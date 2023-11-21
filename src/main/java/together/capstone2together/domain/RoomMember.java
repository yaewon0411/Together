package together.capstone2together.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class RoomMember { //여기는 설문 답변 status = pass 인 회원만 들어옴에 유의
    @Id@GeneratedValue
    @Column(name = "roomMember_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member; //팀원만 속함. 팀장은 room에서 찾을 것

    public static RoomMember create(Room room, Member member){
        if(room.isFull()) throw new IllegalStateException("방에 지정한 인원이 다 찼습니다.");
        RoomMember roomMember = new RoomMember();
        roomMember.setRoom(room);
        roomMember.setMember(member);
        roomMember.getMember().getJoinedRooms().add(roomMember);
        return roomMember;
    }
}
