package together.capstone2together.domain.tag;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
    @Id @GeneratedValue
    @Column(name = "tag_id")
    private Long id;
    @Column(name = "tag_name")
    private String name;

    @Builder
    public Tag(String name){
        this.name = name;
    }
    public static Tag create(String name){
        Tag tag = new Tag();
        tag.setName(name);
        return tag;
    }
}
