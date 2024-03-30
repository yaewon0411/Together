package together.capstone2together.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Survey {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="survey_id")
    private Long id;

    @OneToMany(mappedBy = "survey")
    private List<Question> questions = new ArrayList<>();

    private LocalDateTime localDateTime; //팀원 탭에서는 방이 만들어진 시간으로 보이는 거라서 이거 있어야 함

    public Survey(List<Question> questions){
        this.questions = questions;
        this.localDateTime = LocalDateTime.now();
        for (Question question : questions) {
            question.setSurvey(this);
        }
    }
}
