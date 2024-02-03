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
    @Id @GeneratedValue
    @Column(name="question_id")
    private Long id;
    @Column(columnDefinition = "LONGTEXT")
    private List<String> questionList = new ArrayList<>();

    public Question(List<String> questionList) {
        this.questionList = questionList;
    }
}
