package together.capstone2together.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import together.capstone2together.domain.item.Item;
import together.capstone2together.domain.itemTag.ItemTag;
import together.capstone2together.domain.tag.Tag;
import together.capstone2together.dto.item.ItemReqDto;
import together.capstone2together.dto.item.ItemRespDto;
import together.capstone2together.dto.tag.TagReqDto;
import together.capstone2together.service.ItemService;
import together.capstone2together.service.ItemTagService;
import together.capstone2together.service.TagService;
import together.capstone2together.util.ApiUtils;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final TagService tagService;
    private final ItemTagService itemTagService;

    @PostMapping
    public ResponseEntity<?> addItem(@RequestBody ItemReqDto itemReqDto){

        //아이템 저장
        Item item = itemService.save(itemReqDto);

        //태그  저장
        List<String> tagList = itemReqDto.getTagList();
        for (String tagName : tagList) {
            Optional<Tag> tag = tagService.save(new TagReqDto(tagName));
            tag.ifPresent(value -> itemTagService.save(ItemTag.create(item, value)));
        }
        ItemRespDto itemRespDto = new ItemRespDto(item);
        return new ResponseEntity<>(ApiUtils.success(itemRespDto), HttpStatus.OK);
    }

}
