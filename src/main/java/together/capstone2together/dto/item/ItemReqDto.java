package together.capstone2together.dto.item;

import jakarta.validation.constraints.NotEmpty;
import jdk.jfr.DataAmount;
import lombok.Data;
import lombok.NoArgsConstructor;
import together.capstone2together.domain.item.Item;

import java.util.List;

@Data
public class ItemReqDto {

    @Data
    @NoArgsConstructor
    public static class SearchDto {
        private String title;
        private Long id;
        public SearchDto(Item item){
            title = item.getTitle();
            id = item.getId();
        }
    }

    @Data
    public static class ItemPickReqDto{
        private Long itemId;
        private String status; //pick Status (true or false)
    }

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
