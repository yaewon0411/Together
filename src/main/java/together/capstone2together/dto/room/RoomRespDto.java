package together.capstone2together.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import together.capstone2together.domain.room.Room;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RoomRespDto {

    @Data
    public static class ShowJoinedMemberRespDto{

        private List<MemberInRoomDto> members = new ArrayList<>();
        private String dDay;
        private String title; //방 제목
        private String img;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class MemberInRoomDto {
            private String role;
            private String name;
            private String kakaotalkId;
        }
    }

    @Data
    public static class MakeRoomRespDto{
        private Long id; //방 아이디
        private String title;

        private String memberId;
        public MakeRoomRespDto(Room room){
            this.id = room.getId();
            this.title = room.getTitle();
            this.memberId = room.getMember().getId();
        }
    }
    //    @Query("select new together.capstone2together.dto.RoomDto(r.title, r.content, sa.status, r.member.name, r.survey.localDateTime, r.city, r.id) " +
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomDto{
        private String title;
        private String content;
        private String status;
        private String memberName;
        private LocalDateTime localDateTime;
        private String city;
        private Long id;
    }
}
