package together.capstone2together.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import together.capstone2together.domain.answer.Answer;
import together.capstone2together.domain.member.Member;
import together.capstone2together.domain.room.Room;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyAnswer {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="surveyAnswer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "surveyAnswer")
    private List<Answer> answers = new LinkedList<>(); //답변


    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime localDateTime; //방에 지원한 시간 표기

    public SurveyAnswer (Room room, Member member, List<Answer> answers){
        this.room = room;
        this.member = member;
        this.answers = answers;
        this.status = Status.WAITING;
        for (Answer answer : answers) {
            answer.setSurveyAnswer(this);
        }
    }

    public static SurveyAnswer create(Room room, Member member, List<Answer> answers){
        SurveyAnswer surveyAnswer = new SurveyAnswer(room, member, answers);
        surveyAnswer.setLocalDateTime(LocalDateTime.now());
        return surveyAnswer;
    }

}
