package together.capstone2together.dto.member;

import lombok.*;
import together.capstone2together.domain.member.Member;
import together.capstone2together.util.CustomDateUtil;

public class MemberRespDto {


    @Data
    @NoArgsConstructor
    public static class MyInfoRespDto{
        private int point;
        private String name;
        private int createdRoomCnt;
        private int applyRoomCnt;

        @Builder
        public MyInfoRespDto(int point, String name, int createdRoomCnt, int applyRoomCnt) {
            this.point = point;
            this.name = name;
            this.createdRoomCnt = createdRoomCnt;
            this.applyRoomCnt = applyRoomCnt;
        }
    }

    @Data
    public static class ChangeKakaotalkIdRespDto{
        private String kakaotalkId;

        public ChangeKakaotalkIdRespDto(String kakaotalkId) {
            this.kakaotalkId = kakaotalkId;
        }
    }

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
