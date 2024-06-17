package together.capstone2together.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.answer.Answer;
import together.capstone2together.domain.answer.AnswerRepository;
import together.capstone2together.domain.member.Member;
import together.capstone2together.dto.member.MemberReqDto;
import together.capstone2together.ex.CustomApiException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {

    private final AnswerRepository answerRepository;
    @Transactional
    public void save(Answer answer){
        validateDuplicatedAnswer(answer);
        answerRepository.save(answer);
    }

    private void validateDuplicatedAnswer(Answer answer) {
        Optional<Answer> findOne
                = answerRepository.findById(answer.getId());
        if(findOne.isPresent()) throw new CustomApiException("이미 답변이 존재합니다");
    }
    @Transactional
    public void delete(List<Answer> answers){
        for (Answer answer : answers) {
            answerRepository.delete(answer);
        }
    }
}
