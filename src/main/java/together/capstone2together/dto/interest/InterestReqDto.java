package together.capstone2together.dto.interest;

import lombok.Data;

public class InterestReqDto {

    @Data
    public static class AddInterestToPickReqDto{
        private int score;
        private String memberId;
        private Long itemId;
    }
}
