package together.capstone2together.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/*
다음 순서대로 저장
1. 설문 양식 만들고
2. 설문 질문 만들고
3. 방 만들기
 */
@Entity
@Getter @Setter
@NoArgsConstructor
public class Room {
    @Id @GeneratedValue
    @Column(name="room_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;//팀장 아이디
    private String title; //방 이름
    private String content; //방 소개글
    private int capacity; //방 인원
    private String city; //활동 지역
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Survey survey;
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<RoomMember> roomMemberList = new ArrayList<>();

    public static Room create (Item item, Member member, String title, String content, int capacity, String city, Survey survey) {
        Room room = new Room();
        room.item = item;
        room.member = member;
        room.title = title;
        room.content = content;
        room.capacity = capacity;
        room.city = city;
        room.survey = survey;
        return room;
    }
    public boolean isFull(){
        return roomMemberList.size()>this.capacity; // -> 방 생성자도 이미 들어와있으므로 등호 들어가게
    }
}
