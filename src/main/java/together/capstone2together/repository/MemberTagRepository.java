package together.capstone2together.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.ItemTag;
import together.capstone2together.domain.Member;
import together.capstone2together.domain.MemberTag;
import together.capstone2together.domain.Tag;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberTagRepository {
    private final EntityManager em;

    public void save(MemberTag memberTag){
            validateDuplicatedItemTag(memberTag);
            em.persist(memberTag);
    }
    private void validateDuplicatedItemTag(MemberTag memberTag) {
        List<MemberTag> findList = em.createQuery("select mt from MemberTag mt where mt.member = :member and mt.tag = :tag", MemberTag.class)
                .setParameter("member", memberTag.getMember())
                .setParameter("tag", memberTag.getTag())
                .getResultList();
        if(findList.size()>0) throw new IllegalStateException("이미 관심 태그로 설정되었습니다.");
    }

    public List<MemberTag> findByMember(Member member) {
        return em.createQuery("select mt from MemberTag mt where mt.member = :member", MemberTag.class)
                .setParameter("member",member)
                .getResultList();
    }
    public void delete(MemberTag memberTag){
        em.remove(memberTag);
    }

    public void update(List<MemberTag> memberTagList){
        for (MemberTag memberTag : memberTagList) {
            em.merge(memberTag);
        }
    }
}
