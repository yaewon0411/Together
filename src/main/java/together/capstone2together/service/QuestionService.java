package together.capstone2together.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.Question;
import together.capstone2together.domain.Survey;
import together.capstone2together.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Question findById(Long id){
        Optional<Question> findOne = questionRepository.findById(id);
        if(findOne.isEmpty()) throw new NoResultException("존재하지 않는 질문");
        return findOne.get();
    }
    /*
      "Question" : {
        "1" : "이름은?"
        "2" :  "나이는?"
    }
    */
    @Transactional
    public List<Question> makeQuestion(JsonNode jsonNode) throws JsonProcessingException {
        List<Question> questionList = new LinkedList<>();
        for (JsonNode node : jsonNode) {
            Question question = Question.create(node.textValue());
            questionRepository.save(question);
            questionList.add(question);
        }
        return questionList;
    }

    public JSONObject findQuestions(Survey survey){
        List<Question> questions = questionRepository.findBySurvey(survey);
        JSONObject object = new JSONObject();
        int num = 1;
        for (Question question : questions) {
            object.put(String.valueOf(num),question);
            num++;
        }
        return object;
    }

    @Transactional
    public void deleteQuestion(Question question) {
        questionRepository.delete(question);
    }

    @Transactional
    public void deleteQuestions(List<Question> questions){
        for (Question question : questions) {
            questionRepository.delete(question);
        }
    }
}
