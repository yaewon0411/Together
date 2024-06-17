package together.capstone2together.dto.item;

import lombok.Data;

@Data
public class ItemIdDto {
    private Long id;
    public ItemIdDto(Long id){
        this.id = id;
    }
}
