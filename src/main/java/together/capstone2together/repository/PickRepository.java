package together.capstone2together.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.Item;
import together.capstone2together.domain.Member;
import together.capstone2together.domain.Pick;

import java.util.List;

@Repository
public interface PickRepository extends JpaRepository<Pick, Long> {

    @Query("select i from Pick p join p.item i where p.member = :member")
    List<Item> findByMember(@Param("member") Member member); //마감기한 지나면 거르기

    @Query("select p from Pick p where p.member = :member and p.item = :item")
    List<Pick> findByMemberAndItem(@Param("member")Member member, @Param("item")Item item);
}
