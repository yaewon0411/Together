package together.capstone2together.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

public class QuestionRespDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionMapDto{
        private Map<Integer, String> questions;
    }

}
