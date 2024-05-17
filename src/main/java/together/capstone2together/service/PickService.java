package together.capstone2together.service;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.item.Item;
import together.capstone2together.domain.member.Member;
import together.capstone2together.domain.Pick;
import together.capstone2together.domain.room.Room;
import together.capstone2together.domain.room.RoomService;
import together.capstone2together.dto.pick.PickReqDto;
import together.capstone2together.dto.pick.PickRespDto;
import together.capstone2together.repository.PickRepository;
import together.capstone2together.util.CustomDataUtil;

import java.util.List;
import java.util.stream.Collectors;

import static together.capstone2together.dto.pick.PickReqDto.*;
import static together.capstone2together.dto.pick.PickRespDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PickService {
    private final PickRepository pickRepository;
    private final SubService subService;
    private final RoomService roomService;


    @Transactional
    //찜 저장
    public void save (Pick pick){
        validateDuplicatedPick(pick);
        pickRepository.save(pick);
    }
    //이미 찜한 아이템인지 중복 검사
    private void validateDuplicatedPick(Pick pick) {
        List<Pick> findList = pickRepository.findByMemberAndItem(pick.getMember(), pick.getItem());
        if(findList.size()>0) throw new IllegalStateException("이미 찜한 대외활동 입니다.");
    }
    public JSONArray findByMember(Member member){

        JSONArray array = new JSONArray();
        List<Item> findList = pickRepository.findByMember(member);
        for (Item item : findList) {
            System.out.println("item.getTitle() = " + item.getTitle());
            Room findRoom = roomService.findById(item.getId());
            int size;
            if(findRoom == null) size = 0;
            else size = findRoom.getRoomMemberList().size();
            JSONObject object = new JSONObject();
            array.add(CustomDataUtil.makeObject(item, object, size));
        }
        return array;
    }

  // TODO joinedNumber가 아니라, 해당 아이템에 만들어진 방 개수를 내보내는 걸로!!
    public List<PickItemRespDto> getPickItemList(Member member){
        List<Item> findList = pickRepository.findByMember(member);
        return findList.stream().map(item -> {
            Room room = roomService.findById(item.getId());
            //int roomCountInItem = roomService.getRoomCountInItem(item);
            return PickItemRespDto.builder()
                    .itemId(item.getId())
                    .dDay(CustomDataUtil.makeDday(item.getDeadline()))
                    .img(item.getImg())
                    .views(item.getViews())
                    .createdRoomCount(roomService.getRoomCountInItem(item))
                    .sponsor(item.getSponsor())
                    .title(item.getTitle())
                    .build();
        }).collect(Collectors.toList());
    }





    public boolean findByMemberAndItem(Member member, Item item){
        List<Pick> findList = pickRepository.findByMemberAndItem(member, item);
        if(findList.size()>0) return true;
        else return false;
    }

}
