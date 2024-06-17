package together.capstone2together.service;

import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.*;
import together.capstone2together.domain.member.Member;
import together.capstone2together.domain.member.MemberRepository;
import together.capstone2together.domain.room.Room;
import together.capstone2together.dto.room.RoomReqDto;
import together.capstone2together.dto.surveyAnswer.SurveyAnswerRespDto;
import together.capstone2together.ex.CustomApiException;
import together.capstone2together.repository.RoomMemberRepository;
import together.capstone2together.repository.SurveyAnswerRepository;
import together.capstone2together.util.CustomDateUtil;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static together.capstone2together.dto.room.RoomReqDto.*;
import static together.capstone2together.dto.surveyAnswer.SurveyAnswerRespDto.*;
import static together.capstone2together.dto.surveyAnswer.SurveyAnswerRespDto.AppliedMemberRespDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SurveyAnswerService {
    private final SurveyAnswerRepository surveyAnswerRepository;
    private final MemberRepository memberRepository;
    private final RoomMemberRepository roomMemberRepository;

    @Transactional
    public void save (SurveyAnswer surveyAnswer){
        validateDuplicatedSurveyAnswer(surveyAnswer);
        surveyAnswerRepository.save(surveyAnswer);
    }
    @Transactional
    public void delete(List<SurveyAnswer> surveyAnswers){
        for (SurveyAnswer surveyAnswer : surveyAnswers) {
            surveyAnswerRepository.delete(surveyAnswer);
        }
    }

    public List<SurveyAnswer> findBySurvey(Survey survey){
        return surveyAnswerRepository.findBySurvey(survey);
    }
    private void validateDuplicatedSurveyAnswer(SurveyAnswer surveyAnswer) {
        List<SurveyAnswer> findList =
                surveyAnswerRepository.findByMemberAndRoom(surveyAnswer.getMember().getId(), surveyAnswer.getRoom().getId());
        if(!findList.isEmpty()) throw new CustomApiException("이미 등록한 답변이 존재합니다");
    }
    public SurveyAnswer findById(Long id){
        Optional<SurveyAnswer> findOne = surveyAnswerRepository.findById(id);
        if (findOne.isEmpty()) throw new CustomApiException("설문 답변이 존재하지 않습니다");
        return findOne.get();
    }
    public List<SurveyAnswer> findByMemberExcludeFailSurvey(Member member){
        return surveyAnswerRepository.findByMemberExcludeFailSurvey(member);
    }

    //팀장 탭 - 방을 눌렀을 때 지원한 회원 리스트 뽑기 (방에 지원한 시간 LocalDateTime이라서 String이랑 맵핑이 까다롭네...
    public AppliedMemberRespDto getAppliedMemberList(Room room){

        AppliedMemberRespDto response = new AppliedMemberRespDto();

        List<SurveyAnswer> findList = surveyAnswerRepository.findJoinedMemberByRoom(room.getId());
        response.setTitle(room.getTitle());
        response.setDDay(CustomDateUtil.makeDday(room.getItem().getDeadline()));
        response.setImg(room.getItem().getImg());
        if(findList.isEmpty()) return response;

        for (SurveyAnswer sa : findList) {
            response.getAppliedMemberDtos().add(
                    new AppliedMemberDto(sa, room)
            );
        }
        return response;
    }


    //팀원 탭 - 지원한 방 리스트 보기
    public List<JoinedMemberRespDto> getJoinedRoomList(Member member){
        List<RoomDto> findList = surveyAnswerRepository.findRoomByJoinedMember(member);
        List<JoinedMemberRespDto> response = new ArrayList<>();

        if(findList.isEmpty()) return response;
        for (RoomDto roomDto : findList) {
            response.add(new JoinedMemberRespDto(roomDto));
        }
        return response;
    }

    @Transactional
    public MakeAnswerToPassRespDto makeAnswerToPass(Long surveyAnswerId, Member member) {

        //설문 답변 조회
        SurveyAnswer surveyAnswerPS = surveyAnswerRepository.findById(surveyAnswerId).orElseThrow(
                () -> new CustomApiException("존재하지 않는 답변 입니다")
        );

        //설문 답변을 볼 권한이 있는지 확인
        if(surveyAnswerPS.getRoom().getMember() != member)
            throw new CustomApiException("설문 답변을 볼 권한이 없습니다");

        if(surveyAnswerPS.getStatus() == Status.FAIL)
            throw new CustomApiException("이미 Fail 판정을 내린 답변입니다");

        if(surveyAnswerPS.getStatus() == Status.PASS)
            throw new CustomApiException("이미 Pass 판정을 내린 답변입니다");

        //패스 판정 더티 체킹으로
        surveyAnswerPS.setStatus(Status.PASS);

        RoomMember roomMemberPS = roomMemberRepository.save(
                RoomMember.create(surveyAnswerPS.getRoom(), surveyAnswerPS.getMember())
        );
        return new MakeAnswerToPassRespDto(roomMemberPS, surveyAnswerPS.getStatus());
    }


    //답변에 fail 기능 내리기
    @Transactional
    public void setStatusToFail(Long id){
        SurveyAnswer surveyAnswer = surveyAnswerRepository.findById(id).get();
        if(surveyAnswer.getStatus() == Status.WAITING)
            surveyAnswer.setStatus(Status.FAIL);
    }
    //회원 아이디랑 방 아이디로 설문 답변 찾기
    public List<SurveyAnswerReplyRespDto> findByMemberId(String appliedMemberId, Long roomId) {
        List<SurveyAnswerReplyRespDto> response = new ArrayList<>();
        List<SurveyAnswer> surveyAnswerList = surveyAnswerRepository.findByMemberAndRoom(appliedMemberId, roomId);
        if(surveyAnswerList.isEmpty()) return response;

       memberRepository.findById(appliedMemberId).orElseThrow(
                () -> new CustomApiException("존재하지 않는 지원자 입니다")
        );

        surveyAnswerList.get(0).getAnswers().stream()
                .forEach(
                        a -> response.add(new SurveyAnswerReplyRespDto(a))
                );

        return response;
    }


    @Transactional
    public void makeAnswerToFail(Long surveyAnswerId, Member member) {

        //설문 답변 조회
        SurveyAnswer surveyAnswerPS = surveyAnswerRepository.findById(surveyAnswerId).orElseThrow(
                () -> new CustomApiException("존재하지 않는 답변 입니다")
        );

        //설문 답변을 볼 권한이 있는지 확인
        if(surveyAnswerPS.getRoom().getMember() != member)
            throw new CustomApiException("설문 답변을 볼 권한이 없습니다");

        if(surveyAnswerPS.getStatus() == Status.FAIL)
            throw new CustomApiException("이미 Fail 판정을 내린 답변입니다");

        if(surveyAnswerPS.getStatus() == Status.PASS)
            throw new CustomApiException("이미 Pass 판정을 내린 답변입니다");

        surveyAnswerPS.setStatus(Status.FAIL);
    }
}
