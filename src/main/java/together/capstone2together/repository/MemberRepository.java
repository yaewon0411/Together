package together.capstone2together.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    //findByXXX -> 값 없으면 그냥 NoSuchElement 예외 날리니까 Optional로 받아서 서비스에서 검증

    //카카오톡 아이디 찾기
    @Query("select m from Member m where m.kakaotalkId = :id")
    Member findByKakaotalkId(@Param("id")String id);

    @Modifying(clearAutomatically = true) //영속성 컨텍스트 초기화
    @Query("update Member m set m.name = :newName where m.id = :id")
    void updateName(@Param("id")String id, @Param("newName")String newName);
}
