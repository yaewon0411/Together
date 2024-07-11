package together.capstone2together.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.survey.Survey;
import together.capstone2together.domain.survey.SurveyRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class SurveyService {

    private final SurveyRepository surveyRepository;


    public Survey  save(Survey survey){
        return surveyRepository.save(survey);
    }

    public void delete(Survey survey){
        surveyRepository.delete(survey);
    }


}
