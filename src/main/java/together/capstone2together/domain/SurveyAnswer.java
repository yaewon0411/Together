package together.capstone2together.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class SurveyAnswer {
    @Id @GeneratedValue
    @Column(name="surveyAnswer_id")
    private Long id;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "survey_id")
//    private Survey survey;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;
    private List<String> answer; //답변
    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime localDateTime; //방에 지원한 시간 표기

    public static SurveyAnswer create(Room room, Member member, Question question, List<String> answer){
        SurveyAnswer surveyAnswer = new SurveyAnswer();
        surveyAnswer.setRoom(room);
        surveyAnswer.setMember(member);
        surveyAnswer.setAnswer(answer);
        surveyAnswer.setQuestion(question);
        surveyAnswer.setStatus(Status.WAITING);
        surveyAnswer.setLocalDateTime(LocalDateTime.now());
        return surveyAnswer;
    }
}
