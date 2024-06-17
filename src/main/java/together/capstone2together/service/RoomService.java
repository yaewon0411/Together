package together.capstone2together.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.Question;
import together.capstone2together.domain.SurveyAnswer;
import together.capstone2together.domain.answer.Answer;
import together.capstone2together.domain.answer.AnswerRepository;
import together.capstone2together.domain.item.Item;
import together.capstone2together.domain.item.ItemRepository;
import together.capstone2together.domain.member.Member;
import together.capstone2together.domain.Survey;
import together.capstone2together.domain.room.Room;
import together.capstone2together.domain.room.RoomRepository;
import together.capstone2together.ex.CustomApiException;
import together.capstone2together.repository.QuestionRepository;
import together.capstone2together.repository.RoomMemberRepository;
import together.capstone2together.repository.SurveyAnswerRepository;
import together.capstone2together.repository.SurveyRepository;
import together.capstone2together.util.CustomDateUtil;

import java.util.ArrayList;
import java.util.List;
import static together.capstone2together.dto.item.ItemRespDto.*;
import static together.capstone2together.dto.room.RoomReqDto.*;
import static together.capstone2together.dto.room.RoomRespDto.*;
import static together.capstone2together.dto.room.RoomRespDto.ShowJoinedMemberRespDto.*;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final SurveyRepository surveyRepository;
    private final ItemRepository itemRepository;
    private final AnswerRepository answerRepository;
    private final SurveyAnswerRepository surveyAnswerRepository;
    private final QuestionRepository  questionRepository;

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
        if(findList.size()!=0) throw new CustomApiException("이미 생성한 방이 존재합니다");
    }

    //아이템에 생성된 방 리스트 보기
    public List<ItemListRespDto> findByItemList(Item item){
        List<Room> findList = roomRepository.findRoomsByItem(item);
        List<ItemListRespDto> response = new ArrayList<>();
        if(findList.isEmpty()) return response;
        for (Room room : findList) {
            response.add(new ItemListRespDto(room, roomMemberRepository.roomMemberCount(room)+1));
        }
        return response;
    }

    //팀장이 생성한 방에 모든 지원자가 들어온 경우, 역할과 함께 뿌림
    public ShowJoinedMemberRespDto showJoinedMember(Room room, Member member) {

        // 인원이 다 찬 경우
        ShowJoinedMemberRespDto response = new ShowJoinedMemberRespDto();
        //팀장 넣고
        response.getMembers().add(
                new MemberInRoomDto("팀장", member.getName(), member.getKakaotalkId())
        );
        //팀원 넣기
        roomMemberRepository.findAll(room).stream()
                .forEach(
                        m -> response.getMembers().add(
                                new MemberInRoomDto("팀원",m.getName(), m.getKakaotalkId())
                        )
                );

        response.setTitle(room.getTitle());
        response.setImg(room.getItem().getImg());
        response.setDDay(CustomDateUtil.makeDday(room.getItem().getDeadline()));
        return response;
    }




    //팀장 탭 방 리스트
    public List<CreatedRoomDto> findCreatorRoomList(Member member){
        List<Room> findList = roomRepository.findByMember(member);
        List<CreatedRoomDto> response = new ArrayList<>();
        if(findList.isEmpty()) return response;
        for (Room room : findList) {
            response.add(new CreatedRoomDto(room,roomMemberRepository.roomMemberCount(room)+1));
        }
        return response;
    }

    @Transactional
    public void deleteRoom(Long roomId, Member member) {
        //방 찾기
        Room roomPS = roomRepository.findById(roomId).orElseThrow(
                () -> new CustomApiException("존재하지 않는 방입니다")
        );
        //방을 만든 멤버인지 검증하기
        if(roomPS.getMember() != member)
            throw new CustomApiException("방을 삭제할 권한이 없습니다");

        //방에 pass된 멤버가 있는지 확인하기
        if(!roomPS.getRoomMemberList().isEmpty())
            throw new CustomApiException("방에 참여한 인원이 존재합니다");

        Survey surveyPS = roomPS.getSurvey();

        List<Question> questionsPS = surveyPS.getQuestions();

        List<SurveyAnswer> surveyAnswersPS = surveyAnswerRepository.findBySurvey(surveyPS);

        //개별 답변 삭제
        if(!surveyAnswersPS.isEmpty()) {
            surveyAnswersPS.stream()
                    .map(SurveyAnswer::getAnswers)
                    .forEach(
                            sa -> {
                                answerRepository.deleteAll(sa);
                            }
                    );
        }
        //모든 사용자 설문 총 답변 삭제
        surveyAnswerRepository.deleteAll(surveyAnswersPS);

        //방 삭제
        roomRepository.delete(roomPS);

        //설문 삭제
        surveyRepository.delete(surveyPS);

        //질문 삭제
        questionRepository.deleteAll(questionsPS);
    }




    //방에 인원이 다 찼는 지 검사
    public boolean checkCapacity(Long id){
        Room findOne = roomRepository.findById(id).get();
        int count = roomMemberRepository.roomMemberCount(findOne)+1;
        if(findOne.getCapacity() == count) return true;
        else return false;
    }
    public Room findById(Long itemId){
        return roomRepository.findById(itemId)
                .orElseThrow(
                        () -> new CustomApiException("존재하지 않는 방입니다")
                );
    }

    public int getRoomCountInItem(Item item){
        List<Room> findRooms = roomRepository.findRoomsByItem(item);
        return findRooms.isEmpty()? 0 : findRooms.size();
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
//    public Room makeRoom(Item item, Member member, Survey survey, JsonNode roomNode) {
//        String title = roomNode.get("title").asText();
//        String content = roomNode.get("content").asText();
//        String city = roomNode.get("city").asText();
//        int capacity = roomNode.get("capacity").asInt();
//        Room room = Room.create(item, member, title, content, capacity, city, survey);
//        return room;
//    }



    //컨트롤러 짜면서 추가
    @Transactional
    public void delete(Room room){
        roomRepository.delete(room);
    }

    @Transactional
    public MakeRoomRespDto makeRoom(MakeRoomReqDto makeRoomReqDto, Member member, List<Question> questions) {

        //설문 조사 저장
        Survey surveyPS = surveyRepository.save(new Survey(questions));

        //대외활동 찾기
        Item itemPS = itemRepository.findById(makeRoomReqDto.getMakeRoomDto().getItemId())
                .orElseThrow(
                        () -> new CustomApiException("존재하지 않는 대외활동 입니다")
                );

        Room roomPS = roomRepository.save(Room.create(itemPS, member,
                        makeRoomReqDto.getMakeRoomDto().getTitle(),
                        makeRoomReqDto.getMakeRoomDto().getContent(),
                        makeRoomReqDto.getMakeRoomDto().getCapacity(),
                        makeRoomReqDto.getMakeRoomDto().getCity(),
                        surveyPS
                )
        );

        return new MakeRoomRespDto(roomPS);
    }

}
