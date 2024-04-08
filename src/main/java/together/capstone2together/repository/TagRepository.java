package together.capstone2together.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.Tag;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {


    @Query("select t from Tag t where t.name like %:keyword%")
    List<Tag> searchByTag(@Param("keyword")String keyword);

    Optional<Tag> findByName(String name);

    @Query("select t from Tag t where t.name = :name")
    Tag findOneByName(@Param("name")String name);

    /*

        @Query("SELECT i FROM Item i WHERE " +
            "i.title LIKE %:keyword% OR " +
            "i.content LIKE %:keyword% OR " +
            "i.sponsor LIKE %:keyword% " +
            "and i.img is not null and i.homepage is not null")
    List<Item> searchedItem(@Param("keyword") String keyword);




     */
}
