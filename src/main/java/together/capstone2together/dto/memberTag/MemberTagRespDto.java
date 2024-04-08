package together.capstone2together.dto.memberTag;

import lombok.Data;

@Data
public class MemberTagRespDto {
    private final Long id;
    private final String name;

    public MemberTagRespDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
