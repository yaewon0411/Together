package together.capstone2together.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.Member;
import together.capstone2together.domain.Room;
import together.capstone2together.domain.RoomMember;
import together.capstone2together.dto.ShowAllDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomMemberRepository extends JpaRepository<RoomMember,Long> {

    @Query("select rm from RoomMember rm join fetch rm.room r")
    List<RoomMember> findByMember(Member member);
    List<RoomMember> findByMemberAndRoom(@Param("member")Member member, @Param("room")Room room);

    @Query("select new together.capstone2together.dto.ShowAllDto(m.name, m.kakaotalkId) from RoomMember rm " +
            "left join rm.member m where rm.room = :room")
    List<ShowAllDto> findAll(@Param("room")Room room);

    @Query("select count(m) from RoomMember rm left join rm.member m where rm.room = :room")
    int roomMemberCount(@Param("room")Room room);


    @Query("select count(rm) from RoomMember rm where rm.room.id = :roomId")
    int capacityCount(@Param("roomId")Long roomId);


}
