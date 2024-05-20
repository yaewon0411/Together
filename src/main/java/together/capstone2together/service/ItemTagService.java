package together.capstone2together.service;


import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.*;
import together.capstone2together.domain.item.Item;
import together.capstone2together.domain.memberTag.MemberTag;
import together.capstone2together.domain.room.Room;
import together.capstone2together.domain.room.RoomService;
import together.capstone2together.dto.SearchDto;
import together.capstone2together.repository.ItemTagRepository;
import together.capstone2together.util.CustomDataUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemTagService {

    private final ItemTagRepository itemTagRepository;
    private final SubService subService;
    private final ItemService itemService;

    private final RoomService roomService;
    @Transactional
    public void save(ItemTag itemTag){
        itemTagRepository.save(itemTag);
    }
    public JSONArray findItemByInterestedTag(List<MemberTag> memberTagList){

        //멤버-태그에서 태그 번호 추출
        List<Tag> tagList = new ArrayList<>();
        for (MemberTag memberTag : memberTagList) {
            tagList.add(memberTag.getTag());
        }
        //멤버-태그 기반으로 아이템-태그 리스트 추출
        //List<ItemTag> findList = itemTagRepository.findByTagList(taglist);
        Set<Item> findSet = itemTagRepository.findItemListByTag(tagList);
        List<Item> findList = new ArrayList<>(findSet);
        JSONArray array = new JSONArray();
        for (Item item : findList) {
            List<Room> roomList = roomService.getRoomListByItem(item);
            int size = roomList.size();
            JSONObject object = new JSONObject();
            array.add(CustomDataUtil.makeObject(item, object, size));

        }
        return array;
    }

    private Object makeObject(ItemTag itemTag, JSONObject object) {
        Item findOne = itemTag.getItem();
        object.put("title", findOne.getTitle());
        object.put("itemId",findOne.getId());
        object.put("sponsor",findOne.getSponsor());
        object.put("views",findOne.getViews());
        object.put("img",findOne.getImg());
        object.put("Dday",subService.makeDday(findOne.getDeadline()));

        return object;
    }
    public List<ItemTag> findByTagList(List<Tag>tagList){
        return itemTagRepository.findByTagList(tagList);
    }

    public List<SearchDto> searchItemByTagList(List<Tag> tagList) {
        List<SearchDto> searchList = new ArrayList<>();
        for (Tag tag : tagList) {
            List<ItemTag> findList = itemTagRepository.findBySingleTag(tag);
            for (ItemTag itemTag : findList) {
                System.out.println("itemTag.getItem().getTitle() = " + itemTag.getItem().getTitle());
                SearchDto dto = new SearchDto();
                dto.setId(itemTag.getItem().getId());
                dto.setTitle(itemTag.getItem().getTitle());
                searchList.add(dto);
            }
        }
        return searchList;
    }

//    public List<SearchDto> searchItems(String keyword) {
//        List<SearchDto> result = new ArrayList<>();
//        List<ItemTag> findList = itemTagRepository.find(keyword);
//        for (ItemTag itemTag : findList) {
//            SearchDto dto = new SearchDto();
//            dto.setId(itemTag.getItem().getId());
//            dto.setTitle(itemTag.getItem().getTitle());
//            result.add(dto);
//        }
//        return result;
//    }
//
//    public List<SearchDto> searchItems(String keyword) {
//
//
//        Tag tag = Tag.findByKeyword(keyword);
//        if(tag2 ==null){
//            tag2 = Tag2.containedKeyword(keyword);
//        }
//        System.out.println("tag = " + tag2);
//        List<ItemTag> findList = itemTagRepository.findByTag(tag2);
//        //아이디랑 제목만 내보내면됨
//        List<SearchDto> searchList = new ArrayList<>();
//        for (ItemTag itemTag : findList) {
//            SearchDto dto = new SearchDto();
//            dto.setId(itemTag.getItem().getId());
//            dto.setTitle(itemTag.getItem().getTitle());
//            dto.setTag2List(itemTag.getItem().getTag2List());
//            searchList.add(dto);
//        }
//        return searchList;
//    }
}
