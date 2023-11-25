package together.capstone2together.Controller;

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
import together.capstone2together.service.ItemService;
import together.capstone2together.service.ItemTagService;
import together.capstone2together.service.TagService;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {
    private final ItemService itemService;
    private final TagService tagService;
    private final ItemTagService itemTagService;

    @PostMapping
    public ResponseEntity<String> addItem(@RequestBody ItemDto dto){

        //태그 먼저 저장
        List<String> tagList = dto.getTagList();
        List<Tag> findList = new ArrayList<>();
        for (String name : tagList) {
            Tag tag = Tag.create(name);
            tagService.save(tag);
            findList.add(tagService.findOneByName(tag.getName()));
        }
        //아이템 저장
        Item item = Item.create(dto.getTitle(), dto.getContent(), dto.getSponsor() , dto.getDeadline(), dto.getHomepage(), dto.getImg());
        Item findOne = itemService.save(item);

        //아이템-태그 저장
        List<ItemTag> itemTagList = ItemTag.create(findList, findOne);
        for (ItemTag itemTag : itemTagList) {
            System.out.println("itemTag.getTag().getName() = " + itemTag.getTag().getName());
        }
        itemTagService.save(itemTagList);

        return ResponseEntity.ok("success");
    }

}
