package together.capstone2together.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionRespDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionMapDto{
        private Map<Integer, String> questions;
    }

    @Data
    @NoArgsConstructor
    public static class QuestionAnswerListDto{
        private List<QuestionAnswer> questionAnswerList = new ArrayList<>();
        private Long roomId;
        @Data
        public static class QuestionAnswer{
            public Long questionId;
            private String answer;
        }
    }

}
