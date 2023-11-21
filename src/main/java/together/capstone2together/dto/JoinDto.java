package together.capstone2together.dto;

import lombok.Data;
import together.capstone2together.domain.Tag;
import java.util.List;

@Data
public class JoinDto {
    private String id;
    private String password;
    private String name;
    private String kakaotalkId;
    private List<String> tagList;

}
/*
    public Member(String id, String password, String name, String kakaotalkId, List<Tag> tagList) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.kakaotalkId = kakaotalkId;
        this.tagList = tagList;
        createMemberTag(tagList);
    }
 */