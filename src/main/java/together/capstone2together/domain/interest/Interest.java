package together.capstone2together.domain.interest;

import jakarta.persistence.*;
import lombok.*;
import together.capstone2together.domain.item.Item;
import together.capstone2together.domain.member.Member;
import together.capstone2together.ex.CustomApiException;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public void changeScore(int score){
        if(score<=0) throw new CustomApiException("0점 이하를 줄 수 없습니다");
        this.score = score;
    }

    public static Interest create(Member member, Item item, int score){
        Interest interest = new Interest();
        interest.setMember(member);
        interest.setItem(item);
        interest.setScore(score);
        return interest;
    }

}
