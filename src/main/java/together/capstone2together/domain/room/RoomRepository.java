package together.capstone2together.domain.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.item.Item;
import together.capstone2together.domain.member.Member;
import together.capstone2together.domain.room.Room;


import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {


    List <Room> findByMember(Member member); //팀장 탭 방 리스트 보기
    @Query("select r from Room r where r.item = :item")
    List<Room> findRoomsByItem(@Param("item")Item item);

    Optional<Room> findByItem(Item item);


//    @Query("select r from Room r where r.item = :item and r.member = :member")
//    List<Room> findByMemberAndItem(@Param("item")Item item, @Param("member")Member member);

    Optional<Room> findByMemberAndItem(@Param("item")Item item, @Param("member")Member member);

    @Query("SELECT r FROM Room r WHERE r.item.id IN :itemIds")
    List<Room> findByItemIds(List<Long> itemIds);
}
