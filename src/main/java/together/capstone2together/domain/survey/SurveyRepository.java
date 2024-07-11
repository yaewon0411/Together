package together.capstone2together.domain.survey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.survey.Survey;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

    public Survey save(Survey survey);

}
