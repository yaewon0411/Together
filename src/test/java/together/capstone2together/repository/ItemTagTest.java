package together.capstone2together.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
public class ItemTagTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void 홈탭관심있는태그리스트뽑기(){
        Set<Item> result= 테스트();

        List<Item> resultList = new ArrayList<>(result);

        System.out.println("=================최종 결과==========");
        for (Item item : resultList) {
            System.out.println("itemTag.getItem().getTitle() = " + item.getTitle());
        }
    }
    @Test
    public Set<Item> 테스트 (){

        Member findOne = em.find(Member.class, "rnjsgurdnjs");
        List<MemberTag> memberTagList = findOne.getTagList();

        List<Tag> tagList = new ArrayList<>();
        for (MemberTag memberTag : memberTagList) {
            tagList.add(memberTag.getTag());
        }

        System.out.println("==사용자가 관심있는 태그===");
        for (Tag tag : tagList) {
            System.out.println("tag.getName() = " + tag.getName());
        }
        System.out.println("===========================================");

        if(tagList.size()==0) throw new IllegalStateException("설정한 관심 태그가 없습니다.");
        //List<ItemTag> result = new ArrayList<>();
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


        System.out.println("==============중간 생성된 리스트========");
        for (Item item: resultSet) {
            System.out.println("itemTag.getItem().getTitle() = " + item.getTitle());
        }
        System.out.println("====================");

        if(resultSet.size()<limit){
            int offset = limit - resultSet.size();

            // tagList를 사용하여 ItemTag 리스트를 생성
            List<ItemTag> itemTagList = tagList.stream()
                    .map(tag -> em.createQuery("SELECT it FROM ItemTag it WHERE it.tag = :tag", ItemTag.class)
                            .setParameter("tag", tag)
                            .getResultList())
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            List<ItemTag> addList = em.createQuery("SELECT it FROM ItemTag it WHERE it NOT IN :itemTagList ORDER BY FUNCTION('RAND') LIMIT :count", ItemTag.class)
                    .setParameter("count", offset)
                    .setParameter("itemTagList",itemTagList)
                    //.setFirstResult(0)
                    //.setMaxResults(offset)
                    .getResultList();

            for (ItemTag itemTag : addList) {
                resultSet.add(itemTag.getItem());
            }
            return resultSet;
        }
        System.out.println("result size = " + resultSet.size());
        return resultSet;
    }
}
