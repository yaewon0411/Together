package together.capstone2together.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.*;
import together.capstone2together.domain.item.Item;
import together.capstone2together.domain.member.Member;
import together.capstone2together.repository.InterestRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterestService {
    private final InterestRepository interestRepository;

    @Transactional
    public void save (Interest interest){
        interestRepository.save(interest);
    }

    public void ifPresentPreviousRecordThenDelete(Member member, Item item){
        Optional<Interest> findOne = interestRepository.findByMemberAndItem(member, item);
        findOne.ifPresent(this::delete);
    }
    public void delete(Interest interest){
        interestRepository.delete(interest);
    }


}
