package together.capstone2together.dto.api;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiError {
    private final String msg;
    private final int status;

    public ApiError(String msg, int status) {
        this.msg = msg;
        this.status = status;
    }
}
