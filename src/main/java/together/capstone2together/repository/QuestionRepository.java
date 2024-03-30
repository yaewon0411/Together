package together.capstone2together.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.Question;
import together.capstone2together.domain.Survey;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    public Question save(Question question);

    List<Question> findBySurvey(Survey survey);
}
