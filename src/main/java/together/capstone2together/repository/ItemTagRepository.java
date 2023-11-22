package together.capstone2together.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.Item;
import together.capstone2together.domain.ItemTag;
import together.capstone2together.domain.Tag;
import together.capstone2together.dto.ItemIdDto;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemTagRepository {
    private final EntityManager em;

    public void save(List<ItemTag> itemTagList){
        for (ItemTag itemTag : itemTagList) {
            validateDuplicatedItemTag(itemTag);
            em.persist(itemTag);
        }
    }
    private void validateDuplicatedItemTag(ItemTag itemTag) {
        List <ItemTag> findList = em.createQuery("select it from ItemTag it where it.item = :item and it.tag = :tag", ItemTag.class)
                .setParameter("item", itemTag.getItem())
                .setParameter("tag", itemTag.getTag())
                .getResultList();
        if(findList.size()>0) throw new IllegalStateException("중복 레코드");
    }
    //사용자 관심 태그로 아이템 조회 -> 20개 출력
    /*
    태그 개수에 따라서 내보낼 아이템 결정해야 함
    태그 개수 5개면 각 태그당 4개씩 내보내는 식으로
     */
    public List<ItemTag> findByTagList(List<Tag> tagList){
        if(tagList.size()==0) throw new IllegalStateException("설정한 관심 태그가 없습니다.");
        List<ItemTag> result = new ArrayList<>();
        int limit = 20;
        int size = tagList.size();
        int perSize = limit/size;
        List<ItemTag> subList = new ArrayList<>();

        for (Tag tag : tagList) {
            subList = em.createQuery("" +
                            "select it from ItemTag it " +
                            "left join fetch it.item i where it.tag = :tag and i.available = :Y", ItemTag.class)
                    .setParameter("tag", tag)
                    .setParameter("Y","Y")
                    .setFirstResult(0)
                    .setMaxResults(perSize)
                    .getResultList();

            if(result.size()==limit) return result;
            result.addAll(subList);
        }
        if(result.size()<limit){
            int offset = limit - result.size();

            List<ItemTag> addList = em.createQuery("SELECT it FROM ItemTag it WHERE it NOT IN :subList ORDER BY FUNCTION('RAND') LIMIT :count", ItemTag.class)
                    .setParameter("count", offset)
                    .setParameter("subList",subList)
                    .setFirstResult(0)
                    .setMaxResults(limit - offset)
                    .getResultList();
            result.addAll(addList);
            return result;
        }
        return result;
    }
    public List<ItemTag> findByTag(Tag tag){
        return em.createQuery("select it from ItemTag it where it.tag = :tag",ItemTag.class)
                .setParameter("tag", tag)
                .setMaxResults(20)
                .getResultList();
    }
    public List<ItemTag> findBySingleTag(Tag tag){
        return em.createQuery("select it from ItemTag it where it.tag = :tag", ItemTag.class)
                .setParameter("tag",tag)
                .getResultList();
    }
    public List<ItemIdDto> findItemByItemTag(ItemTag itemTag){
        return em.createQuery("select new together.capstone2together.dto.ItemIdDto(i.id) from ItemTag it left join it.item i where i = :item", ItemIdDto.class)
                .setParameter("item",itemTag.getItem())
                .getResultList();
    }

}
