package together.capstone2together.dto.item;

import jakarta.validation.constraints.NotEmpty;
import jdk.jfr.DataAmount;
import lombok.Data;
import together.capstone2together.domain.item.Item;

import java.util.List;

@Data
public class ItemReqDto {

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    private String sponsor;

    @NotEmpty
    private String deadline;

    private String homepage;

    @NotEmpty
    private String img;

    private List<String> tagList;

    public Item toEntity(){
        return Item.create(
                this.title,
                this.content,
                this.sponsor,
                this.deadline,
                this.homepage,
                this.img
        );
    }
}
