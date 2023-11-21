package together.capstone2together.dto;

import lombok.Data;
import together.capstone2together.domain.Room;
import together.capstone2together.domain.Status;

import java.time.LocalDateTime;

@Data
public class RoomDto { //팀원 모집 화면

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

