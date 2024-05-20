package together.capstone2together.dto;

import lombok.Data;
import together.capstone2together.domain.Tag;

import java.util.List;

@Data
public class TagListReqDto {
    private List<String> tagList;
}
