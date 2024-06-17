package together.capstone2together.domain.itemTag;

import jakarta.persistence.*;
import lombok.*;
import together.capstone2together.domain.Tag;
import together.capstone2together.domain.item.Item;

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
