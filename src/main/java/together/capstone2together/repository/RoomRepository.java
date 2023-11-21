package together.capstone2together.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.Item;
import together.capstone2together.domain.Member;
import together.capstone2together.domain.Room;


import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {


    List <Room> findByMember(Member member); //팀장 탭 방 리스트 보기
    @Query("select r from Room r where r.item = :item")
    List<Room> findByItem(@Param("item")Item item);

    @Query("select r from Room r where r.item = :item and r.member = :member")
    List<Room> findByMemberAndItem(@Param("item")Item item, @Param("member")Member member);


}
