package together.capstone2together.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@AllArgsConstructor
public class Survey {

    @Id @GeneratedValue
    @Column(name="survey_id")
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    private Question question;
    private LocalDateTime localDateTime; //팀원 탭에서는 방이 만들어진 시간으로 보이는 거라서 이거 있어야 함
    public Survey() {

    }
    public Survey(Question question){
        this.question = question;
        this.localDateTime = LocalDateTime.now();
    }
}
