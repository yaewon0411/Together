package together.capstone2together.dto;

import lombok.Data;

import java.util.List;

@Data
public class ItemDto {
    private String title;
    private String content;
    private String sponsor;
    private String deadline;
    private List<String> tagList;
}
