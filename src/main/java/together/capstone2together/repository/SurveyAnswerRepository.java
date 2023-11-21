package together.capstone2together.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.Member;
import together.capstone2together.domain.Room;
import together.capstone2together.domain.SurveyAnswer;
import together.capstone2together.dto.MemberDto;
import together.capstone2together.dto.RoomDto;

import java.util.List;

@Repository
public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer,Long> {

    @Query("select sa from SurveyAnswer sa join sa.member m where sa.room.id = :id")
    List<SurveyAnswer> findJoinedMemberByRoom(@Param("id") Long id);    //팀장 탭 - 방을 눌렀을 때 지원한 회원 리스트 뽑기

//    @Query("select new together.capstone2together.dto.RoomDto(r.title, r.content, sa.status, r.member.name, r.survey.localDateTime, r.city) " +
//            "from SurveyAnswer sa " +
//            "join sa.room r " +
//            "where sa.member = :member")
    //@Query("select sa from SurveyAnswer sa left join sa.room r left join sa.member m where sa.member = :member")
    @Query("select new together.capstone2together.dto.RoomDto(r.title, r.content, sa.status, r.member.name, r.survey.localDateTime, r.city, r.id) " +
            "from SurveyAnswer sa " +
            "join sa.room r " +
            "where sa.member = :member")
    List<RoomDto> findRoomByJoinedMember(@Param("member")Member member); //팀원 탭 - 지원한 방 조회 기능

    @Query("select sa from SurveyAnswer sa where sa.member.id = :memberId and sa.room.id = :roomId")
    List<SurveyAnswer> findByMemberAndRoom(@Param("memberId")String memberId, @Param("roomId")Long roomId);

    @Query("select sa from SurveyAnswer sa where sa.member = :member and sa.status != FAIL")
    List<SurveyAnswer> findByMemberExcludeFailSurvey(@Param("member")Member member);

}
