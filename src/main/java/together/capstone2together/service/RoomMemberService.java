package together.capstone2together.service;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.*;
import together.capstone2together.dto.RoomMemberDto;
import together.capstone2together.dto.ShowAllDto;
import together.capstone2together.repository.MemberRepository;
import together.capstone2together.repository.RoomMemberRepository;
import together.capstone2together.repository.RoomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomMemberService {
    private final RoomMemberRepository roomMemberRepository;
    private final RoomRepository roomRepository;

    //설문 답변 pass한 회원 방에 조인시키기
    @Transactional
    public void save(RoomMember roomMember){
        validateDuplicatedRoomMember(roomMember);
        roomMemberRepository.save(roomMember);
    }
    private void validateDuplicatedRoomMember(RoomMember roomMember) {
        List<RoomMember> findList =
                roomMemberRepository.findByMemberAndRoom(roomMember.getMember(), roomMember.getRoom());
        if(findList.size()>0) throw new IllegalStateException("이미 참여한 회원");
    }
    //팀 구성원 직책과 연락처 확인 화면
    public JSONArray findAllMemberInRoom(Long id){
        Room room = roomRepository.findById(id).get();
        List<ShowAllDto> findList = roomMemberRepository.findAll(room);

        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();

        object.put("role","팀장");
        object.put("name",room.getMember().getName());
        object.put("kakaotalkId",room.getMember().getKakaotalkId());
        array.add(object);

        //팀원 반복문 돌려서 넣기
        for (ShowAllDto show : findList) {
            JSONObject object2 = new JSONObject();
            object2.put("role","팀원");
            object2.put("name",show.getName());
            object2.put("kakaotalkId",show.getKakaotalkId());
            array.add(object2);
        }
        return array;
    }
    public boolean trueJoinedMember(Member member, Room room){
        List<RoomMember> findList = roomMemberRepository.findByMemberAndRoom(member, room);
        if(findList.size()>0) return true;
        else return false;
    }
}
