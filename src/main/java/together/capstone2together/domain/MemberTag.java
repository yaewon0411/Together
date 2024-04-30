package together.capstone2together.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import together.capstone2together.domain.member.Member;


@Entity
@Getter@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTag {
    @Id @GeneratedValue
    @Column(name = "memberTag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public static MemberTag create(Member member, Tag tag){
        MemberTag memberTag = new MemberTag();
        memberTag.setMember(member);
        memberTag.setTag(tag);
        return memberTag;
    }
}
