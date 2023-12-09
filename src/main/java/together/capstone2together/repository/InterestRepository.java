package together.capstone2together.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.AI;
import together.capstone2together.domain.Interest;
import together.capstone2together.domain.Item;
import together.capstone2together.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest,Long> {

    Optional<Interest> findByMemberAndItem(Member member, Item item);
}
