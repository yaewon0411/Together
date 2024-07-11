package together.capstone2together.dto.surveyAnswer;

import lombok.Data;
import lombok.NoArgsConstructor;
import together.capstone2together.domain.answer.Answer;
import together.capstone2together.domain.roomMember.RoomMember;
import together.capstone2together.domain.Status;
import together.capstone2together.domain.surveyAnswer.SurveyAnswer;
import together.capstone2together.domain.room.Room;
import together.capstone2together.dto.room.RoomDto;
import together.capstone2together.util.CustomDateUtil;

import java.util.ArrayList;
import java.util.List;

public class SurveyAnswerRespDto {

    @Data
    @NoArgsConstructor
    public static class MakeAnswerToPassRespDto{
        private String id;
        private String status;
        private Long roomId;

        public MakeAnswerToPassRespDto(RoomMember roomMember, Status status) {
            this.id = roomMember.getMember().getId();
            this.status = status.toString();
            this.roomId = roomMember.getRoom().getId();
        }
    }

    @Data
    @NoArgsConstructor
    public static class JoinedMemberRespDto{
        private String title;
        private String content;
        private String creator;
        private String city;
        private String createdTime;
        private String status;
        private Long roomId;

        public JoinedMemberRespDto(RoomDto roomDto) {
            this.title = roomDto.getTitle();
            this.content = roomDto.getContent();
            this.creator = roomDto.getCreator();
            this.city = roomDto.getCity();
            this.createdTime = CustomDateUtil.makeAppliedDay(roomDto.getLocalDateTime());
            this.status = roomDto.getStatus().toString();
            this.roomId = roomDto.getRoomId();
        }
    }


    @Data
    @NoArgsConstructor
    public static class SurveyAnswerReplyRespDto{
        private String question;
        private String reply;

        public SurveyAnswerReplyRespDto(Answer answer) {
            this.question = answer.getQuestion().getAsking();
            this.reply = answer.getReply();
        }
    }

    @Data
    public static class AppliedMemberRespDto{
        private String title;
        private String dDay;
        private List<AppliedMemberDto> appliedMemberDtos = new ArrayList<>();
        private String img;

        @Data
        @NoArgsConstructor
        public static class AppliedMemberDto{
            private String memberId;
            private Long roomId;
            private String name;
            private String appliedDay;
            private String status;
            private Long surveyAnswerId;

            public AppliedMemberDto(SurveyAnswer sa, Room room) {
                this.memberId = sa.getMember().getId();
                this.roomId = room.getId();
                this.name = sa.getMember().getName();
                this.appliedDay = CustomDateUtil.makeAppliedDay(sa.getLocalDateTime());
                this.status = sa.getStatus().toString();
                this.surveyAnswerId = sa.getId();
            }
        }
    }


}
