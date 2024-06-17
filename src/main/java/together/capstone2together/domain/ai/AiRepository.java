package together.capstone2together.domain.ai;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.ai.AI;
import together.capstone2together.domain.member.Member;

import java.util.List;

@Repository
public interface AiRepository extends JpaRepository<AI, Long> {

    List<AI> findByMember(Member member);

}
