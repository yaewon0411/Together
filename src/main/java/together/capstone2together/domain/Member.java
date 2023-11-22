package together.capstone2together.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter
@NoArgsConstructor @AllArgsConstructor
public class Member implements Serializable {
    @Id @Column(name = "member_id", unique = true)
    private String id;
    private String password;
    private String name;
    @Column(name = "kakaotalk_id", unique = true)
    private String kakaotalkId;
    private int point;



    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<MemberTag> tagList = new ArrayList<>();


    public Member(String id, String password, String name, String kakaotalkId) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.kakaotalkId = kakaotalkId;
    }

    //private String storageId; //대외활동 관심도 평가 가중치 저장소
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Room> ledRooms = new ArrayList<>(); //회원이 팀장으로서 생성한 방
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<RoomMember> joinedRooms = new ArrayList<>(); //회원이 팀원으로서 참여한 방. 팀장 id는 들어가지 x
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Pick> pickList = new ArrayList<>();

}
