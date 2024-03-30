package together.capstone2together.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
