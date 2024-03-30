package together.capstone2together.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surveyAnswer_id")
    private SurveyAnswer surveyAnswer;

    @Column(columnDefinition = "LONGTEXT")
    private String reply;

    public Answer(Question question, String reply){
        this.question = question;
        this.reply = reply;
    }
    public static Answer create(Question question, String reply){
        Answer answer = new Answer(question, reply);
        return answer;
    }


}
