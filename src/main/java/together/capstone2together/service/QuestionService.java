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

    @Transactional
    public Question makeQuestion(JsonNode jsonNode) throws JsonProcessingException {
        List<String> questionList = new ArrayList<>();
        for (JsonNode node1 : jsonNode) {
            questionList.add(node1.textValue());
        }
        Question question = new Question(questionList);
        return  questionRepository.save(question);
    }
    public JSONObject showQuestionList(Question question){
        List<String> questionList = question.getQuestionList();
        JSONObject object = new JSONObject();
        int num = 1;
        for (String s : questionList) {
            object.put(String.valueOf(num), s);
            num++;
        }
        return object;
    }

}
