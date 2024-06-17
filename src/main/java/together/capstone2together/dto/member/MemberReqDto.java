package together.capstone2together.dto.member;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import together.capstone2together.domain.member.Member;

import java.util.List;

public class MemberReqDto {

    @Data
    public static class LoginReqDto{
        @NotEmpty
        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자 2~20자 이내로 작성해주세요")
        private String username;

        @NotEmpty
        @Size(min = 4, max = 20) //string 타입 길이 검증
        private String password;
    }

    @Data
    public static class ChangePwReqDto{
        private String password;

        public ChangePwReqDto(String password) {
            this.password = password;
        }
    }

    @Data
    public static class ChangeKakaotalkIdReqDto{
        private String kakaotalkId;
    }

    @Data
    public static class JoinReqDto{
        @NotEmpty
        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자 2~20자 이내로 작성해주세요")
        private String id;

        @NotEmpty
        @Size(min = 4, max = 20) //string 타입 길이 검증
        private String password;

        @NotEmpty
        @Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$", message = "한글/영문 1~20자 이내로 작성해주세요")
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
