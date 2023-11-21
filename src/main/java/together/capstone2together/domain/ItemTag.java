package together.capstone2together.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;
import together.capstone2together.service.ItemTagService;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

@Entity
@Getter @Setter
@NoArgsConstructor
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

    public static List<ItemTag> create(List<Tag>tagList, Item item){
        List<ItemTag> itemTagList =  new ArrayList<>();
        for (Tag tag : tagList) {
            ItemTag itemTag = new ItemTag();
            itemTag.setTag(tag);
            itemTag.setItem(item);
            item.getTagList().add(itemTag);
        }
        return itemTagList;
    }
}
