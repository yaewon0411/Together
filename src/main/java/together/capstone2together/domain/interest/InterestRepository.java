package together.capstone2together.domain.interest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.interest.Interest;
import together.capstone2together.domain.item.Item;
import together.capstone2together.domain.member.Member;

import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest,Long> {

    Optional<Interest> findByMemberAndItem(Member member, Item item);
}
