package together.capstone2together.domain.member;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import together.capstone2together.domain.Pick;
import together.capstone2together.domain.room.Room;
import together.capstone2together.domain.RoomMember;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Member implements Serializable {
    @Id @Column(name = "member_id", unique = true)
    private String id;
    private String password;
    @Column(length = 20)
    private String name;
    @Column(name = "kakaotalk_id", unique = true)
    private String kakaotalkId;
    private int point;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // insert, update 할 때
    @Column(nullable = false)
    private LocalDateTime updatedAt;


    @Builder
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
