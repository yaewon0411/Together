package together.capstone2together.domain;

import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.A;
import together.capstone2together.service.ItemTagService;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ItemTag")
public class ItemTag implements Serializable {
    @Id @GeneratedValue
    @Column(name = "itemTag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="item_id")
    private Item item;

    public static ItemTag create(Item item, Tag tag){
        ItemTag itemTag = new ItemTag();
        itemTag.setItem(item);
        itemTag.setTag(tag);
        itemTag.getItem().getTagList().add(itemTag);
        return itemTag;
    }

}
