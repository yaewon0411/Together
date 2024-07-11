package together.capstone2together.dto.tag;

import lombok.Data;
import together.capstone2together.domain.tag.Tag;

import java.util.List;

@Data
public class TagReqDto {

    @Data
    public static class TagListReqDto {
        private List<String> tagList;
    }


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
