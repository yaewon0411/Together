package together.capstone2together.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import together.capstone2together.domain.ItemTag;
import together.capstone2together.domain.Tag;
import together.capstone2together.dto.SearchDto;
import together.capstone2together.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final ItemTagService itemTagService;

    public void save (Tag tag){
        if(validateDuplicatedTag(tag)) return;
        tagRepository.save(tag);
    }

    private boolean validateDuplicatedTag(Tag tag) {
        List<Tag> findList = tagRepository.findByName(tag.getName());
        if(findList.size()>0) return true;
        else return false;
    }
    public Tag findOneByName(String name){
        return tagRepository.findOneByName(name);
    }


    public List<SearchDto> searchItems(String keyword) {
        List<Tag> tagList = tagRepository.searchByTag(keyword);
        //아이템테그 레포에서 태그리스트의 이름을 갖는 아이템을 찾기
        for (Tag tag : tagList) {
            System.out.println("tag.getName() = " + tag.getName());
        }
        List<SearchDto> findList = itemTagService.searchItemByTagList(tagList);
        //List<ItemTag> findList = itemTagService.findByTagList(tagList);

//        for (ItemTag itemTag : findList) {
//            System.out.println("itemTag = " + itemTag);
//        }
//
//        //아이디랑 제목만 내보내면됨
//        for (ItemTag itemTag : findList) {
//            SearchDto dto = new SearchDto();
//            dto.setTitle(itemTag.getItem().getTitle());
//            dto.setId(itemTag.getItem().getId());
//            searchList.add(dto);
//        }
        //return searchList;
        return findList;
    }
}
