package together.capstone2together.domain.itemTag;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.Tag;

import java.util.List;

@Repository
public interface ItemTagRepositoryI extends JpaRepository<ItemTag, Long> {

    @Query("select it from ItemTag it where it.tag = :tag")
    List<ItemTag> findDistinctByTag(Tag tag, Pageable pageable);

    List<ItemTag> findByTag(Tag tag);
}
