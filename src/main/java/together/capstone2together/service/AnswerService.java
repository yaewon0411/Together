package together.capstone2together.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.Answer;
import together.capstone2together.repository.AnswerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {

    private final AnswerRepository answerRepository;
    @Transactional
    public void save(Answer answer){
        answerRepository.save(answer);
    }
    @Transactional
    public void delete(List<Answer> answers){
        for (Answer answer : answers) {
            answerRepository.delete(answer);
        }
    }
}
