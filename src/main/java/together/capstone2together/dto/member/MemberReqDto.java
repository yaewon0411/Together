package together.capstone2together.dto.member;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import together.capstone2together.domain.Member;
import together.capstone2together.domain.Tag;

import java.util.List;

public class MemberReqDto {
    @Data
    public static class JoinReqDto{
        @NotEmpty
        private String id;
        @NotEmpty
        private String password;
        @NotEmpty
        private String name;
        @NotEmpty
        private String kakaotalkId;
        private List<String> tagList;


        public Member toEntity(BCryptPasswordEncoder passwordEncoder){
            return Member.builder()
                    .id(this.id)
                    .password(passwordEncoder.encode(this.password))
                    .name(this.name)
                    .kakaotalkId(this.kakaotalkId)
                    .build();
        }

    }
}
