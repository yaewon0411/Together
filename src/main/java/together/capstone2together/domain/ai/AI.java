package together.capstone2together.domain.ai;

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
public class AI { //ai가 자정 지난 후 해당 멤버에게 추천할 아이템 번호들을 저장하는 테이블

    @Id @GeneratedValue
    @Column(name=  "Ai_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

}
