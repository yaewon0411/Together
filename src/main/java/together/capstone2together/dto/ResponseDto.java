package together.capstone2together.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
public class ResponseDto<T> {
    private final int code; //실패:-1, 정상:1
    private final String message;
    private final T data;

    public ResponseDto(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
