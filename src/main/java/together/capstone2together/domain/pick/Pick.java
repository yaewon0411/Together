package together.capstone2together.domain.pick;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import together.capstone2together.domain.item.Item;
import together.capstone2together.domain.member.Member;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pick { //유저가 찜한 대외활동

    @Id @GeneratedValue
    @Column(name = "pick_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item; //이 부분 얘기해봐야할듯

    public static Pick create(Member member, Item item){
        Pick pick = new Pick();
        pick.setMember(member);
        pick.setItem(item);
        pick.getMember().getPickList().add(pick);
        return pick;
    }
}
