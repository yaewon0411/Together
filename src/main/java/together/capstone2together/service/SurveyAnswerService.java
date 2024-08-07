package together.capstone2together.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.controller.TeamMemberController;
import together.capstone2together.domain.*;
import together.capstone2together.domain.answer.Answer;
import together.capstone2together.domain.answer.AnswerRepository;
import together.capstone2together.domain.member.Member;
import together.capstone2together.domain.member.MemberRepository;
import together.capstone2together.domain.question.Question;
import together.capstone2together.domain.room.Room;
import together.capstone2together.domain.roomMember.RoomMember;
import together.capstone2together.domain.survey.Survey;
import together.capstone2together.domain.surveyAnswer.SurveyAnswer;
import together.capstone2together.dto.room.RoomDto;
import together.capstone2together.dto.surveyAnswer.SurveyAnswerReqDto;
import together.capstone2together.ex.CustomApiException;
import together.capstone2together.domain.question.QuestionRepository;
import together.capstone2together.domain.roomMember.RoomMemberRepository;
import together.capstone2together.domain.surveyAnswer.SurveyAnswerRepository;
import together.capstone2together.util.CustomDateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static together.capstone2together.dto.question.QuestionRespDto.*;
import static together.capstone2together.dto.question.QuestionRespDto.QuestionAnswerListDto.*;
import static together.capstone2together.dto.surveyAnswer.SurveyAnswerReqDto.*;
import static together.capstone2together.dto.surveyAnswer.SurveyAnswerRespDto.*;
import static together.capstone2together.dto.surveyAnswer.SurveyAnswerRespDto.AppliedMemberRespDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SurveyAnswerService {
    private final SurveyAnswerRepository surveyAnswerRepository;
    private final MemberRepository memberRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

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

    //팀장 탭 - 방을 눌렀을 때 지원한 회원 리스트 뽑기
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
    public Object surveyAnswerStatus(Long surveyAnswerId, Member member, SurveyStatusReqDto surveyStatusReqDto){
        if(surveyStatusReqDto.getStatus().equals("pass")) return makeAnswerToPass(surveyAnswerId, member);
        else return makeAnswerToFail(surveyAnswerId, member);

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
    public MakeAnswerToFailRespDto makeAnswerToFail(Long surveyAnswerId, Member member) {

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

        return new MakeAnswerToFailRespDto(surveyAnswerPS.getStatus());
    }

    public void applyRoom(QuestionAnswerListDto questionAnswerListDto, Room room, Member member) {

        List<Answer> answers = new ArrayList<>();
        // answer 등록하고
        List<QuestionAnswer> questionAnswerList = questionAnswerListDto.getQuestionAnswerList();
        for (QuestionAnswer questionAnswer : questionAnswerList) {
            Question questionPS = questionRepository.findById(questionAnswer.getQuestionId())
                    .orElseThrow(
                            () -> new CustomApiException("해당 질문은 존재하지 않습니다")
                    );
            Answer answerPS = answerRepository.save(Answer.create(questionPS, questionAnswer.getAnswer()));
            answers.add(answerPS);
        }

        // surveyAnswer 등록하기
        surveyAnswerRepository.save(SurveyAnswer.create(room, member, answers));
    }
}
