package together.capstone2together.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
public class Interest { //ai 추천 대외활동에 대한 관심도(1-5) 저장. score>3이면 Pick으로
    @Id@GeneratedValue
    @Column(name="interest_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;
    private int score; //4~5점 인 거는 내가 관심 있는 거

    public static Interest create(Member member, Item item, int score){
        Interest interest = new Interest();
        interest.setMember(member);
        interest.setItem(item);
        interest.setScore(score);
        return interest;
    }

}
