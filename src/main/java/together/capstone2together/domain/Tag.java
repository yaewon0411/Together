package together.capstone2together.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Tag {
    @Id @GeneratedValue
    @Column(name = "tag_id")
    private Long id;
    @Column(name = "tag_name")
    private String name;

    public static Tag create(String name){
        Tag tag = new Tag();
        tag.setName(name);
        return tag;
    }
}
