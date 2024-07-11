package together.capstone2together.dto.memberTag;

import lombok.Builder;
import lombok.Data;
import together.capstone2together.domain.tag.Tag;
import together.capstone2together.domain.member.Member;

public class MemberTagReqDto {

    @Data
    public static class MemberTagSaveReqDto{
        private Member member;
        private Tag tag;

        @Builder
        public MemberTagSaveReqDto(Member member, Tag tag) {
            this.member = member;
            this.tag = tag;
        }
    }
}
