package together.capstone2together.dto;

import lombok.Data;
import together.capstone2together.domain.Status;

import java.time.LocalDateTime;

@Data
public class MemberDto { //팀장이 만든 방에 지원한 회원
    private String name;
    private LocalDateTime localDateTime;
    private Status status;

    public MemberDto(String name, LocalDateTime localDateTime, Status status) {
        this.name = name;
        this.localDateTime = localDateTime;
        this.status = status;
    }
}
