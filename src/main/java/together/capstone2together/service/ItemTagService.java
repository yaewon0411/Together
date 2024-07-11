package together.capstone2together.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.itemTag.ItemTag;
import together.capstone2together.domain.itemTag.ItemTagRepositoryI;
import together.capstone2together.domain.memberTag.MemberTag;
import together.capstone2together.domain.room.Room;
import together.capstone2together.domain.room.RoomRepository;
import together.capstone2together.domain.tag.Tag;

import java.util.*;

import static together.capstone2together.dto.item.ItemReqDto.*;
import static together.capstone2together.dto.item.ItemRespDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemTagService {

    private final RoomRepository roomRepository;
    private final ItemTagRepositoryI itemTagRepositoryI;
    private final int TOTAL_ITEM = 20;
    @Transactional
    public void save(ItemTag itemTag){
        itemTagRepositoryI.save(itemTag);
    }

    public List<ItemByInterestRespDto> findItemByInterestedTag(List<MemberTag> memberTagList){

        List<ItemByInterestRespDto> response = new ArrayList<>();
        //멤버-태그에서 태그 번호 추출
        List<Tag> tagList = memberTagList.stream().map(MemberTag::getTag).toList();

        //tagList가 20개가 넘으면 -> 몇 개를 선택해서 아이템 서치
        if(tagList.size() > TOTAL_ITEM){
            Collections.shuffle(tagList);
            tagList = tagList.subList(0, TOTAL_ITEM);
        }

        int itemsPerTag = TOTAL_ITEM / tagList.size();
        int remainder = TOTAL_ITEM % tagList.size();

        for(int i = 0; i < tagList.size(); i++){
            int itemCount = itemsPerTag + (i<remainder? 1 : 0);
            Pageable pageable = PageRequest.of(0, itemCount);
            List<ItemTag> itemTagPC = itemTagRepositoryI.findDistinctByTag(tagList.get(i), pageable);
            itemTagPC.stream().forEach(
                    itemTag -> {
                        Optional<Room> roomOP = roomRepository.findByItem(itemTag.getItem());
                        roomOP.ifPresent(room -> response.add(new ItemByInterestRespDto(room, itemTag.getItem())));
                    }
            );
        }
        return response;
    }


    public List<SearchDto> searchItemByTagList(List<Tag> tagList) {
        List<SearchDto> searchList = new ArrayList<>();
        for (Tag tag : tagList) {
            List<ItemTag> findList = itemTagRepositoryI.findByTag(tag);
            findList.stream()
                    .map(itemTag -> new SearchDto(itemTag.getItem()))
                    .forEach(searchList::add);
        }
        return searchList;
    }

}
