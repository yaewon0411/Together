package together.capstone2together.dto.pick;

import lombok.Builder;
import lombok.Data;

public class PickRespDto {

    @Data
    @Builder
    public static class PickItemRespDto{
        private Long itemId;
        private String sponsor;
        private String img;
        private String dDay;
        private String title;
        private int joinedNumber;
        private int views;

        public PickItemRespDto(Long itemId, String sponsor, String img, String dDay, String title, int joinedNumber, int views) {
            this.itemId = itemId;
            this.sponsor = sponsor;
            this.img = img;
            this.dDay = dDay;
            this.title = title;
            this.joinedNumber = joinedNumber;
            this.views = views;
        }
    }
}
