package together.capstone2together.dto.item;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import together.capstone2together.domain.itemTag.ItemTag;
import together.capstone2together.domain.item.Item;
import together.capstone2together.domain.room.Room;
import together.capstone2together.util.CustomDataUtil;
import together.capstone2together.util.CustomDateUtil;

import java.util.List;
import java.util.stream.Collectors;

@ToString
@Data
public class ItemRespDto {

    @Data
    public static class ItemListRespDto{
        private String title;
        private String content;
        private String createdDay;
        private String creator;
        private int joinedNumber;
        private Long itemId;
        private int capacity;
        private String city;
        private Long roomId;

        public ItemListRespDto(Room room, int joinedNumber) {
            this.title = room.getTitle();
            this.content = room.getContent();
            this.createdDay = CustomDateUtil.makeCreatedDay(room.getSurvey().getLocalDateTime());
            this.creator = room.getMember().getName();
            this.joinedNumber = joinedNumber;
            this.itemId = room.getItem().getId();
            this.capacity = room.getCapacity();
            this.city = room.getCity();
            this.roomId = room.getId();
        }
    }

    private String title;
    private String createdAt;

    public ItemRespDto(Item item){
        this.title = item.getTitle();
        this.createdAt = CustomDateUtil.toStringFormat(item.getCreatedAt());
    }

    @Data
    public static class ItemByInterestRespDto{
        private String title;
        private Long itemId;
        private String sponsor;
        private int views;
        private String img;
        private String dDay;
        private int joinedNumber;
        public ItemByInterestRespDto (Room room, Item item){
            this.title = room.getTitle();
            this.itemId = item.getId();
            this.sponsor = item.getSponsor();
            this.views = item.getViews();
            this.img = item.getImg();
            this.dDay = CustomDataUtil.makeDday(item.getDeadline());
            this.joinedNumber = room.getRoomMemberList().size();
        }
    }
    @Data
    @NoArgsConstructor
    public static class ItemInfoRespDto{
        private String title;
        private String content;
        private String img;
        private int views;
        private String dDay;
        private String sponsor;
        private String deadline;
        private String homepage;
        private List<TagDto> tags;

        public ItemInfoRespDto(Item item) {
            this.title = item.getTitle();
            this.content = item.getContent();
            this.img = item.getImg();
            this.views = item.getViews();
            this.dDay = CustomDataUtil.makeDday(item.getDeadline());
            this.sponsor = item.getSponsor();
            this.deadline = item.getDeadline();
            this.homepage = item.getHomepage();
            this.tags = TagDto.createTagDto(item.getTagList());
        }

        @NoArgsConstructor
        public static class TagDto{
            private String name;

            public TagDto(String name) {
                this.name = name;
            }

            public static List<TagDto> createTagDto (List<ItemTag> tags) {
                return tags.stream().map(t -> new TagDto(t.getTag().getName())).collect(Collectors.toList());
            }
        }
    }
    @Data
    @NoArgsConstructor
    public static class Top20ViewsRespDto {
        private String title;
        private Long itemId;
        private String sponsor;
        private int views;
        private String img;
        private String dDay;
        private int joinedNumber;
        public Top20ViewsRespDto (Item item){
            this.title = item.getTitle();
            this.itemId = item.getId();
            this.sponsor = item.getSponsor();
            this.views = item.getViews();
            this.img = item.getImg();
            this.dDay = CustomDataUtil.makeDday(item.getDeadline());
            this.joinedNumber = item.getRoomList().size();
        }
    }

    @Data
    @NoArgsConstructor
    public static class ImminentDeadlineRespDto {
        private String title;
        private Long itemId;
        private String sponsor;
        private int views;
        private String img;
        private String dDay;
        private int joinedNumber;
        public ImminentDeadlineRespDto (Room room, Item item){
            this.title = room.getTitle();
            this.itemId = item.getId();
            this.sponsor = item.getSponsor();
            this.views = item.getViews();
            this.img = item.getImg();
            this.dDay = CustomDataUtil.makeDday(item.getDeadline());
            this.joinedNumber = room.getRoomMemberList().size();
        }
    }
    @Data
    @NoArgsConstructor
    public static class RecentlyAddRespDto {
        private String title;
        private Long itemId;
        private String sponsor;
        private int views;
        private String img;
        private String dDay;
        private int joinedNumber;
        public RecentlyAddRespDto (Room room, Item item){
            this.title = room.getTitle();
            this.itemId = item.getId();
            this.sponsor = item.getSponsor();
            this.views = item.getViews();
            this.img = item.getImg();
            this.dDay = CustomDataUtil.makeDday(item.getDeadline());
            this.joinedNumber = room.getRoomMemberList().size();
        }
    }
}
