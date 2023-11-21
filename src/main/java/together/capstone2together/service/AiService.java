package together.capstone2together.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.AI;
import together.capstone2together.domain.Item;
import together.capstone2together.domain.Member;
import together.capstone2together.repository.AiRepository;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class AiService {
    private final AiRepository aiRepository;

    public List<Item> findByMember(Member member){
        List<AI> findList = aiRepository.findByMember(member);
        return findList.stream().map(AI::getItem).toList();
    }
    public Item getOne(Member member){
        List<AI> findList = aiRepository.findByMember(member);
        int random = (int)(Math.random()*findList.size());
        AI findOne = findList.get(random);
        return findOne.getItem();
    }
}
