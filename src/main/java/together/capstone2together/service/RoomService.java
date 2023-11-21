package together.capstone2together.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.Item;
import together.capstone2together.domain.Member;
import together.capstone2together.domain.Room;
import together.capstone2together.domain.Survey;
import together.capstone2together.dto.CreatorRoomDto;
import together.capstone2together.repository.ItemRepository;
import together.capstone2together.repository.RoomMemberRepository;
import together.capstone2together.repository.RoomRepository;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final SubService subService;

    //방 생성
    @Transactional
    public void save (Room room){
        validateDuplicatedRoom(room);
        roomRepository.save(room);
    }

    //중복 방 생성 검사
    private void validateDuplicatedRoom(Room room) {
        //회원 아이디 찾고
        List<Room> findList = roomRepository.findByMemberAndItem(room.getItem(), room.getMember());
        //그 회원이 동일 아이템에 대해 생성한 방이 있는지
        if(findList.size()!=0) throw new IllegalStateException("중복 생성");
    }



    //아이템에 생성된 방 리스트 보기
    public JSONArray findByItem(Item item){
        List<Room> findList = roomRepository.findByItem(item);
        JSONArray array = new JSONArray();
        if(findList.size()==0) return array;
        for (Room room : findList) {
            JSONObject object = new JSONObject();
            object.put("title",room.getTitle());
            object.put("content",room.getContent());
            object.put("createdDay",subService.makeCreatedDay(room.getSurvey().getLocalDateTime()));
            object.put("creator",room.getMember().getName());
            object.put("joinedNumber",roomMemberRepository.roomMemberCount(room));
            object.put("itemId",room.getItem().getId());
            array.add(object);
        }
        return array;
    }
    //팀장 탭 방 리스트
    public JSONArray findCreatorRoomList(Member member){
        List<Room> findList = roomRepository.findByMember(member);
        if(findList.size()==0) return new JSONArray();

        JSONArray result = new JSONArray();

        for (Room room : findList) {
            JSONObject object = new JSONObject();
            object.put("title",room.getTitle());
            object.put("content",room.getContent());
            object.put("deadline",subService.makeDday(room.getItem().getDeadline()));
            object.put("joinedNumber",roomMemberRepository.roomMemberCount(room));
            object.put("capacity",room.getCapacity());
            object.put("roomId",room.getId());
            result.add(object);
        }
        return result;
    }
    //방에 인원이 다 찼는 지 검사
    public boolean checkCapacity(Long id){
        Room findOne = roomRepository.findById(id).get();
        int count = roomMemberRepository.roomMemberCount(findOne)+1;
        if(findOne.getCapacity() == count) return true;
        else return false;
    }
    public Room findById(Long id){
        return roomRepository.findById(id).get();
    }


    /*
      "Room" : {
	"title" : "방 제목",
	"content" : "방 소개글",
	"city" : "제주",
	"capacity" : "2명",
	"memberId" : "팀장 아이디"
	"itemId" : "아이템 아이디"
     }
     */
    public Room makeRoom(Item item, Member member, Survey survey, JsonNode roomNode) {
        String title = roomNode.get("title").asText();
        String content = roomNode.get("content").asText();
        String city = roomNode.get("city").asText();
        int capacity = roomNode.get("capacity").asInt();
        Room room = Room.create(item, member, title, content, capacity, city, survey);
        return room;
    }
    //컨트롤러 짜면서 추가
}
