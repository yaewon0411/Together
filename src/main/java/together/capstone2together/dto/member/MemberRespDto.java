package together.capstone2together.dto.member;

import lombok.Data;
import lombok.ToString;
import together.capstone2together.domain.member.Member;
import together.capstone2together.util.CustomDateUtil;

public class MemberRespDto {

    @Data
    public static class LoginRespDto{
        private String id;
        private String createdAt;

        public LoginRespDto(Member member) {
            this.id = member.getId();
            this.createdAt = CustomDateUtil.toStringFormat(member.getCreatedAt());
        }
    }
    @Data
    public static class JoinRespDto{
        private String id;
        private String name;

        public JoinRespDto(Member member) {
            this.id = member.getId();
            this.name = member.getName();
        }
    }
}
