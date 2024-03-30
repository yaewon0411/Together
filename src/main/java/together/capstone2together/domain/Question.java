package together.capstone2together.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="question_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @Column(columnDefinition = "LONGTEXT")
    private String asking;

    public Question(String asking){
        this.asking = asking;
    }
    public static Question create(String asking){
        Question question = new Question(asking);
        return question;
    }


//    @Column(columnDefinition = "LONGTEXT")
//    private List<String> questionList = new ArrayList<>();

//    public Question(List<String> questionList) {
//        this.questionList = questionList;
//    }
}
