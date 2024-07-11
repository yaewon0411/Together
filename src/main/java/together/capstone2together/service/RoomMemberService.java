package together.capstone2together.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.room.Room;
import together.capstone2together.domain.roomMember.RoomMember;
import together.capstone2together.domain.member.Member;
import together.capstone2together.ex.CustomApiException;
import together.capstone2together.domain.roomMember.RoomMemberRepository;
import together.capstone2together.domain.room.RoomRepository;

import java.util.List;

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

    public Room trueJoinedMember(Member member, Long roomId){
        Room roomPS = roomRepository.findById(roomId).orElseThrow(
                () -> new CustomApiException("존재하지 않는 방입니다")
        );

        List<RoomMember> findList = roomMemberRepository.findByMemberAndRoom(member, roomPS);
        if(findList.isEmpty()) throw new CustomApiException("해당 방에 Fail 판정을 받았습니다");
        return roomPS;
    }
}
