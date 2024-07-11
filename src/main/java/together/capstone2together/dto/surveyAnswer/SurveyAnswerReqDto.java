package together.capstone2together.dto.surveyAnswer;

import lombok.Data;
import lombok.NoArgsConstructor;

public class SurveyAnswerReqDto {

    @Data
    @NoArgsConstructor
    public static class SurveyStatusReqDto{
        String status;
    }
}
