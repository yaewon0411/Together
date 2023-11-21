package together.capstone2together.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.Interest;
import together.capstone2together.domain.Pick;
import together.capstone2together.repository.InterestRepository;
import together.capstone2together.repository.PickRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterestService {
    private final InterestRepository interestRepository;

    @Transactional
    public void save (Interest interest){
        interestRepository.save(interest);
    }

}
