package together.capstone2together.dto.item;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import together.capstone2together.domain.item.Item;
import together.capstone2together.util.CustomDateUtil;

@ToString
@Data
public class ItemRespDto {

    private String title;
    private String createdAt;

    public ItemRespDto(Item item){
        this.title = item.getTitle();
        this.createdAt = CustomDateUtil.toStringFormat(item.getCreatedAt());
    }
}
