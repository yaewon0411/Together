package together.capstone2together.domain.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.survey.Survey;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {


    List<Question> findBySurvey(Survey survey);
}
