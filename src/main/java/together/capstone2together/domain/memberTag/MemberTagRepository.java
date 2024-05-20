package together.capstone2together.domain.memberTag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import together.capstone2together.domain.Tag;
import together.capstone2together.domain.member.Member;

import java.util.List;

public interface MemberTagRepository extends JpaRepository<MemberTag, Long> {

    boolean existsByMemberAndTag(Member member, Tag tag);

    @Query("delete from MemberTag mt where mt.member = :member")
    void deleteByMember(@Param("member") Member member);

    List<MemberTag> findByMember(Member member);
}
