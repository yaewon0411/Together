package together.capstone2together.domain.answer;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.answer.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
