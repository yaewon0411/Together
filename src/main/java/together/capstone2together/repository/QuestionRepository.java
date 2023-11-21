package together.capstone2together.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    public Question save(Question question);

}
