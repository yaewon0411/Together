package together.capstone2together.dto.tag;

import lombok.Data;
import together.capstone2together.domain.Tag;

import java.util.List;

@Data
public class TagReqDto {

    private String name;
    public TagReqDto(String name){
        this.name = name;
    }
    public Tag toEntity(){
        return Tag.builder()
                .name(this.name)
                .build();
    }

}
