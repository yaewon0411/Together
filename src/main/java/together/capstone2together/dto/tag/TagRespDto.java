package together.capstone2together.dto.tag;

import lombok.Data;
import together.capstone2together.domain.tag.Tag;

@Data
public class TagRespDto {
    private Long id;
    private String name;
    public TagRespDto(Tag tag){
        this.id = tag.getId();
        this.name = tag.getName();
    }

}
