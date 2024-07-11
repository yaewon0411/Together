package together.capstone2together.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.tag.Tag;
import together.capstone2together.dto.tag.TagReqDto;
import together.capstone2together.domain.tag.TagRepository;

import java.util.List;
import java.util.Optional;

import static together.capstone2together.dto.item.ItemReqDto.*;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {

    private final TagRepository tagRepository;
    private final ItemTagService itemTagService;

    public Optional<Tag> save(TagReqDto tagReqDto){
        if(!validateDuplicatedTag(tagReqDto)) {
            Tag tag = tagRepository.save(tagReqDto.toEntity());
            return Optional.of(tag);
        }
        return Optional.empty();
    }

    private boolean validateDuplicatedTag(TagReqDto tagReqDto) {
        Optional<Tag> tagOp = tagRepository.findByName(tagReqDto.getName());
        return tagOp.isPresent();
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
