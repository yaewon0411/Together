package together.capstone2together.dummy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import together.capstone2together.domain.item.Item;
import together.capstone2together.domain.member.Member;

import java.time.LocalDateTime;

public class DummyObject {

    protected Member mockMember(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");
        return  Member.builder()
                .id("root1234")
                .password(encPassword)
                .kakaotalkId("rootKakao")
                .name("root")
                .build();
    }
    protected Item mockItem(){
        return Item.builder()
                .img("img1")
                .content("item content")
                .title("item title")
                .sponsor("item sponsor")
                .build();
    }
}
