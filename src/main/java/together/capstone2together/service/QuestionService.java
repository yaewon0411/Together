package together.capstone2together.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.Question;
import together.capstone2together.domain.Survey;
import together.capstone2together.dto.question.QuestionRespDto;
import together.capstone2together.dto.room.RoomReqDto;
import together.capstone2together.repository.QuestionRepository;

import java.util.*;
import java.util.stream.Collectors;

import static together.capstone2together.dto.question.QuestionRespDto.*;
import static together.capstone2together.dto.room.RoomReqDto.*;
import static together.capstone2together.dto.room.RoomReqDto.MakeRoomReqDto.*;

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
    public List<Question> makeQuestion(MakeRoomReqDto makeRoomReqDto) throws JsonProcessingException {

        return makeRoomReqDto.getQuestionDtoList().stream()
                .map(q -> questionRepository.save(Question.create(q.getAsking())))
                .filter(Optional::isPresent)
                .map(q -> q.get())
                .collect(Collectors.toList());

    }

    public QuestionMapDto findQuestions(Survey survey){
        List<Question> questions = questionRepository.findBySurvey(survey);
        Map<Integer, String> questionsMap = new LinkedHashMap<>();
        int num = 1;
        for (Question question : questions) {
            questionsMap.put(num++,question.getAsking());
        }
        return new QuestionMapDto(questionsMap);
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
