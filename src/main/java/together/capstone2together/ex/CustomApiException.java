package together.capstone2together.ex;

import java.util.Map;

public class CustomApiException extends RuntimeException{

    public CustomApiException(String message) {
        super(message);
    }
}
