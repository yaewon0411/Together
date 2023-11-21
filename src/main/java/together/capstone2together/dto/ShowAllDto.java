package together.capstone2together.dto;

import lombok.Data;

@Data
public class ShowAllDto {
    String name;
    String kakaotalkId;

    public ShowAllDto(String name, String kakaotalkId) {
        this.name = name;
        this.kakaotalkId = kakaotalkId;
    }
}
