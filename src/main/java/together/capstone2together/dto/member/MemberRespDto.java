package together.capstone2together.dto.member;

import lombok.Data;
import together.capstone2together.domain.Member;

import java.util.ArrayList;
import java.util.List;

public class MemberRespDto {
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
