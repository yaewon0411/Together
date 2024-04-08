package together.capstone2together.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import together.capstone2together.domain.Item;
import together.capstone2together.domain.ItemTag;
import together.capstone2together.domain.Tag;
import together.capstone2together.dto.ItemDto;
import together.capstone2together.dto.tag.TagReqDto;
import together.capstone2together.service.ItemService;
import together.capstone2together.service.ItemTagService;
import together.capstone2together.service.TagService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {
    private final ItemService itemService;
    private final TagService tagService;
    private final ItemTagService itemTagService;

    @PostMapping
    public ResponseEntity<String> addItem(@RequestBody ItemDto dto){

        //아이템 저장
        Item item = itemService
                .save(Item.create(dto.getTitle(), dto.getContent(), dto.getSponsor() , dto.getDeadline(), dto.getHomepage(), dto.getImg()));

        //태그  저장
        List<String> tagList = dto.getTagList();
        for (String tagName : tagList) {
            Optional<Tag> tag = tagService.save(new TagReqDto(tagName));
            tag.ifPresent(value -> itemTagService.save(ItemTag.create(item, value)));
        }

        return ResponseEntity.ok("success");
    }

}
