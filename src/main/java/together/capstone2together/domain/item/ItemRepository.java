package together.capstone2together.domain.item;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.item.Item;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @EntityGraph(attributePaths = {"roomList"})
    @Query("select i from Item i where i.deadline >= :currentTime order by i.views desc")
    List<Item> findTop20ByViews(@Param("currentTime")String currentTime);

    List<Item> findTop20ByDeadlineAfterOrderByDeadlineAsc(String currentTime);


    @Query("select i from Item i where i.deadline >= :currentTime order by i.id desc")
    List<Item> findTop20ByOrderByIdDesc(@Param("currentTime")String currentTime);

//    @Query("select i from Item i " +
//            "where i.title like %:keyword% or i.content like %:keyword% or i.sponsor like %:keyword% or " +
//            ":tag IN elements(i.tagList)")
//    List<Item> searchedItem(@Param("keyword")String keyword, @Param("tag")Tag tag);

    //태그가 없을 수도 있음
    @Query("SELECT i FROM Item i WHERE " +
            "(i.title LIKE %:keyword% OR " +
            "i.content LIKE %:keyword% OR " +
            "i.sponsor LIKE %:keyword%) " +
            "AND i.img IS NOT NULL AND i.homepage IS NOT NULL")
    List<Item> searchedItem(@Param("keyword") String keyword);

    Optional<Item> findByTitleAndDeadline(String title, String deadline);


}
