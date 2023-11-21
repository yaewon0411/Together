package together.capstone2together.service;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import together.capstone2together.domain.*;
import together.capstone2together.dto.RoomDto;
import together.capstone2together.repository.SurveyAnswerRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SurveyAnswerService {
    private final SurveyAnswerRepository surveyAnswerRepository;
    private final SubService subService;

    @Transactional
    public void save (SurveyAnswer surveyAnswer){
        validateDuplicatedSurveyAnswer(surveyAnswer);
        surveyAnswerRepository.save(surveyAnswer);
    }
    private void validateDuplicatedSurveyAnswer(SurveyAnswer surveyAnswer) {
        List<SurveyAnswer> findList =
                surveyAnswerRepository.findByMemberAndRoom(surveyAnswer.getMember().getId(), surveyAnswer.getRoom().getId());
        if(findList.size()>0) throw new IllegalStateException("답변 중복 등록 시도");
    }
    public SurveyAnswer findById(Long id){
        Optional<SurveyAnswer> findOne = surveyAnswerRepository.findById(id);
        if (findOne.isEmpty()) throw new NoResultException("설문 답변이 존재하지 않음");
        return findOne.get();
    }
    public List<SurveyAnswer> findByMemberExcludeFailSurvey(Member member){
        return surveyAnswerRepository.findByMemberExcludeFailSurvey(member);
    }

    //팀장 탭 - 방을 눌렀을 때 지원한 회원 리스트 뽑기 (방에 지원한 시간 LocalDateTime이라서 String이랑 맵핑이 까다롭네...
    public JSONArray getAppliedMemberList(Long id){
        JSONArray array = new JSONArray();

        List<SurveyAnswer> findList = surveyAnswerRepository.findJoinedMemberByRoom(id);
        for (SurveyAnswer sa : findList) {
            if(sa.getStatus() == Status.FAIL) continue; //이미 fail 판정 내린 건 리스트에서 제외시키기
            JSONObject object = new JSONObject();
            object.put("memberId",sa.getMember().getId());
            object.put("roomId",sa.getRoom().getId());
            object.put("name",sa.getMember().getName());
            object.put("appliedDay" ,subService.makeAppliedDay(sa.getLocalDateTime()));
            object.put("status",sa.getStatus());
            array.add(object);
        }
        return array; //일단 막아놓고 테스트
    }

    //팀원 탭 - 지원한 방 리스트 보기
    public JSONArray getJoinedRoomList(Member member){
        List<RoomDto> findList = surveyAnswerRepository.findRoomByJoinedMember(member);
        JSONArray array = new JSONArray();

        for (RoomDto dto : findList) {
            JSONObject object = new JSONObject();
            object.put("title", dto.getTitle());
            object.put("content",dto.getContent());
            object.put("creator", dto.getCreator());
            object.put("city", dto.getCity());
            object.put("createdTime",subService.makeAppliedDay(dto.getLocalDateTime())); //이거 이상하면 아래걸로 바꾸기
            //object.put("createdTime",makeCreatedDay(dto.getLocalDateTime())); //방이 생성된 시간이거는 makeApplied에서 current랑 deadline 순서 바뀌어야 함
            object.put("status",dto.getStatus());
            object.put("roomId", dto.getRoomId());
            array.add(object);
        }
        return array;
    }
    private String makeCreatedDay(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String createdDay= localDateTime.format(formatter);

        LocalDate currentDate = LocalDate.now();

        String[] dateParts = createdDay.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

        LocalDate createdDate = LocalDate.of(year, month, day);

        long hours = Duration.between(createdDate.atStartOfDay(), currentDate.atStartOfDay()).toHours();

        if (hours >= 24) {
            int days = (int) hours / 24; // 일(day)로 변환
            return days + "일 전";
        } else {
            return (int) hours + "시간 전";
        }
    }

    //답변에 pass 기능 내리기 -> roomMember에 들어감
    @Transactional
    public void setStatusToPass(Long id){
        SurveyAnswer surveyAnswer = surveyAnswerRepository.findById(id).get();
        if(surveyAnswer.getStatus() == Status.WAITING)
            surveyAnswer.setStatus(Status.PASS);
    }
    //답변에 fail 기능 내리기
    @Transactional
    public void setStatusToFail(Long id){
        SurveyAnswer surveyAnswer = surveyAnswerRepository.findById(id).get();
        if(surveyAnswer.getStatus() == Status.WAITING)
            surveyAnswer.setStatus(Status.FAIL);
    }
    //회원 아이디랑 방 아이디로 설문 답변 찾기
    public JSONArray findByMemberId(String memberId, Long roomId) {
        SurveyAnswer findOne = surveyAnswerRepository.findByMemberAndRoom(memberId, roomId).get(0);
        List<String> findQuestion = findOne.getQuestion().getQuestionList();
        List<String> findAnswer = findOne.getAnswer();

        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        obj.put("surveyAnswerId",findOne.getId());
        array.add(obj);
        for(int i = 0;i<findQuestion.size();i++){
            JSONObject object = new JSONObject();
            object.put(findQuestion.get(i),findAnswer.get(i));
            array.add(object);
        }
        return array;
    }
}
