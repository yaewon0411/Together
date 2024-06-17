package together.capstone2together.dto.room;

import lombok.Data;
import together.capstone2together.domain.Status;
import together.capstone2together.domain.room.Room;
import together.capstone2together.util.CustomDateUtil;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class RoomReqDto {

    @Data
    public static class CreatedRoomDto{
        private String title;
        private String content;
        private int joinedNumber;
        private int capacity;
        private String deadline;
        private Long roomId;

        public CreatedRoomDto(Room room, int joinedNumber) {
            this.title = room.getTitle();
            this.content = room.getContent();
            this.joinedNumber = joinedNumber;
            this.capacity = room.getCapacity();
            this.roomId = room.getId();
            this.deadline = CustomDateUtil.makeDday(room.getItem().getDeadline());
        }
    }

    @Data
    public static class MakeRoomReqDto{
        private List<QuestionDto> questionDtoList;
        private MakeRoomDto  makeRoomDto;

        @Data
        public static class QuestionDto{
            private Long id;
            private String asking;
        }
        @Data
        public static class MakeRoomDto{
            private String title;
            private String content;
            private String city;
            private Long itemId;
            private String asking;
            private int capacity;
        }
    }

    @Data
    public static class RoomDto { //팀원 모집 화면

        private String title;
        private String content;
        private Status status;
        private String creator;
        private LocalDateTime localDateTime;
        private String city;
        private Long roomId;
        public RoomDto(String title, String content, Status status, String creator, LocalDateTime localDateTime, String city, Long roomId) {
            this.title = title;
            this.content = content;
            this.status = status;
            this.creator = creator;
            this.localDateTime = localDateTime;
            this.city = city;
            this.roomId = roomId;
        }
    }
}
