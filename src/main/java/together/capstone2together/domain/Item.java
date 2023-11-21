package together.capstone2together.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Item implements Serializable { //크롤링 결과 저장
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_id")
    private Long id;
    private String title;//제목
    private String content;//내용
    private String sponsor;//주최기관
    private String img;//이미지
    private String deadline;//마감기한 -> "yyyy-mm-dd"으로
    private String homepage;//이거 추가할 것 -> 주최 기관 홈페이지 -> 컨트롤러에서 이것도 내보내도록 수정해야함
    private int views; //조회수

    @OneToMany(mappedBy = "item",cascade = CascadeType.REMOVE)
    private List<ItemTag> tagList = new ArrayList<>();

    @Column(name = "available")
    private String available; //마감 기한 지났는 지 여부 -> 쓸까말까 고민

    @PrePersist
    @PostLoad //아이템 select 할 때마다 시행되게 ->일단 테스트 해보기~~~~~
    public void updateAvailability() {
        if (deadline != null && LocalDate.parse(deadline).isBefore(LocalDate.now())) {
            available = "N"; // 마감 기한이 지났다면 'N'으로 표기
        } else {
            available = "Y"; // 아직 마감 기한이 남았다면 'Y'으로 표기
        }
    }

    public static Item create(String title, String content, String sponsor, String deadline){
        Item item = new Item();
        item.setTitle(title);
        item.setContent(content);
        item.setSponsor(sponsor);
        item.setDeadline(deadline);
        return item;
    }
}
