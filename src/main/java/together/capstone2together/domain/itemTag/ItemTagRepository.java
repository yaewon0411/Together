package together.capstone2together.domain.itemTag;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.item.Item;
import together.capstone2together.domain.tag.Tag;
import together.capstone2together.dto.item.ItemIdDto;
import together.capstone2together.ex.CustomApiException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemTagRepository {
    private final EntityManager em;

    public void save(ItemTag itemTag){
        em.persist(itemTag);
    }

    //사용자 관심 태그로 아이템 조회 -> 20개 출력
    /*
    태그 개수에 따라서 내보낼 아이템 결정해야 함
    태그 개수 5개면 각 태그당 4개씩 내보내는 식으로
     */
    public Set<Item> findItemListByTag(List<Tag> tagList){
        if(tagList.isEmpty()) throw new CustomApiException("설정한 관심 태그가 없습니다.");
        int limit = 20;
        int size = tagList.size(); //6
        int perSize = limit/size; //3

        Set<Item> resultSet = new HashSet<>(); // 중복을 방지하기 위한 Set

        for (Tag tag : tagList) {
            List<ItemTag> findList = em.createQuery("" +
                            "select distinct it from ItemTag it " +
                            "left join fetch it.item i where it.tag = :tag and i.available = :Y", ItemTag.class)
                    .setParameter("tag", tag)
                    .setParameter("Y", "Y")
                    .setFirstResult(0)
                    .setMaxResults(perSize)
                    .getResultList();

            if(resultSet.size()==limit) return resultSet;

            for (ItemTag itemTag : findList) {
                resultSet.add(itemTag.getItem());
            }
        }
        if(resultSet.size()<limit){
            int offset = limit - resultSet.size();

            // tagList를 사용하여 ItemTag 리스트를 생성
            List<ItemTag> itemTagList = tagList.stream()
                    .map(tag -> em.createQuery("SELECT it FROM ItemTag it WHERE it.tag = :tag", ItemTag.class)
                            .setParameter("tag", tag)
                            .getResultList())
                    .flatMap(List::stream)
                    .toList();

            List<ItemTag> addList = em.createQuery("SELECT it FROM ItemTag it WHERE it.id NOT IN :itemTagList", ItemTag.class)
                    .setParameter("itemTagList", itemTagList.stream().map(ItemTag::getId).collect(Collectors.toList()))
                    .setMaxResults(offset)
                    .getResultList();
            for (ItemTag itemTag : addList) {
                resultSet.add(itemTag.getItem());
            }
            return resultSet;
        }
        return resultSet;
    }
    public List<ItemTag> findByTagList(List<Tag> tagList){
        if(tagList.isEmpty()) throw new CustomApiException("설정한 관심 태그가 없습니다.");
        List<ItemTag> result = new ArrayList<>();
        int limit = 20;
        int size = tagList.size(); //6
        int perSize = limit/size; //3

        for (Tag tag : tagList) {
            List<ItemTag> findList = em.createQuery("" +
                            "select distinct it from ItemTag it " +
                            "left join fetch it.item i where it.tag = :tag and i.available = :Y", ItemTag.class)
                    .setParameter("tag", tag)
                    .setParameter("Y","Y")
                    .setFirstResult(0)
                    .setMaxResults(perSize)
                    .getResultList();

            if(result.size()==limit) return result;
            findList.stream().
                    filter(itemTag -> !result.contains(itemTag))
                            .forEach(result::add);
        }
        if(result.size()<limit){
            int offset = limit - result.size();
            List<ItemTag> addList = em.createQuery("SELECT it FROM ItemTag it WHERE it NOT IN :result ORDER BY FUNCTION('RAND') LIMIT :count", ItemTag.class)
                    .setParameter("count", offset)
                    .setParameter("result",result)
                    .setFirstResult(0)
                    .setMaxResults(offset)
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
        return em.createQuery("select new together.capstone2together.dto.item.ItemIdDto(i.id) from ItemTag it left join it.item i where i = :item", ItemIdDto.class)
                .setParameter("item",itemTag.getItem())
                .getResultList();
    }

}
