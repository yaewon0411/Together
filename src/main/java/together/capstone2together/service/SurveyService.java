package together.capstone2together.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.Room;
import together.capstone2together.domain.Survey;
import together.capstone2together.repository.SurveyRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyService {

    private final SurveyRepository surveyRepository;

    public Survey  save(Survey survey){
        return surveyRepository.save(survey);
    }


}
