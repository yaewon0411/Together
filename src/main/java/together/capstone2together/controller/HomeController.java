package together.capstone2together.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import together.capstone2together.config.auth.LoginMember;
import together.capstone2together.domain.item.Item;
import together.capstone2together.domain.memberTag.MemberTag;
import together.capstone2together.domain.pick.Pick;
import together.capstone2together.domain.question.Question;
import together.capstone2together.service.MemberTagService;
import together.capstone2together.domain.room.Room;
import together.capstone2together.service.RoomService;
import together.capstone2together.domain.member.Member;
import together.capstone2together.service.MemberService;
import together.capstone2together.service.*;
import together.capstone2together.util.ApiUtils;

import java.util.*;

import static together.capstone2together.dto.item.ItemReqDto.*;
import static together.capstone2together.dto.item.ItemRespDto.*;
import static together.capstone2together.dto.question.QuestionRespDto.*;
import static together.capstone2together.dto.room.RoomReqDto.*;
import static together.capstone2together.dto.room.RoomRespDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home") // 홈 탭
public class HomeController {

    private final ItemService itemService;
    private final ItemTagService itemTagService;
    private final MemberService memberService;
    private final MemberTagService memberTagService;
    private final RoomService roomService;
    private final SurveyAnswerService surveyAnswerService;
    private final QuestionService questionService;
    private final PickService pickService;
    private final TagService tagService;
    private final AnswerService answerService;

    @GetMapping("/popular") //실시간 인기 활동
    public ResponseEntity<?> getPopularItem(@AuthenticationPrincipal LoginMember loginMember){
        List<Top20ViewsRespDto> top20Views = itemService.getTop20Views();
        return new ResponseEntity<>(ApiUtils.success(top20Views),HttpStatus.OK);
    }
    @GetMapping("/imminent-deadline") //마감 직전 활동
    public ResponseEntity<?> getImminentDeadLine(@AuthenticationPrincipal LoginMember loginMember){
        List<ImminentDeadlineRespDto> imminentDeadline = itemService.getImminentDeadline();
        return new ResponseEntity<>(ApiUtils.success(imminentDeadline),HttpStatus.OK);
    }

    @GetMapping("/interest") //관심 있는 활동
    public ResponseEntity<?> getItemByInterestedTag(@AuthenticationPrincipal LoginMember loginMember){
        List<MemberTag> tagList = memberTagService.findByMember(loginMember.getMember());
        List<ItemByInterestRespDto> itemByInterestedTag = itemTagService.findItemByInterestedTag(tagList);
        return new ResponseEntity<>(ApiUtils.success(itemByInterestedTag),HttpStatus.OK);
    }

    @GetMapping("/recent") //최근 추가된 활동
    public ResponseEntity<?> getRecentlyAddItem(@AuthenticationPrincipal LoginMember loginMember){
        List<RecentlyAddRespDto> recentlyAddedItem = itemService.getRecentlyAddedItem();
        return new ResponseEntity<>(ApiUtils.success(recentlyAddedItem),HttpStatus.OK);
    }


    @GetMapping("/items/{itemId}") //홈 화면에서 특정 아이템 누르면 그 아이템의 상세 정보가 나오도록
    public ResponseEntity<?> getItemInfo(@PathVariable("itemId") Long itemId, @AuthenticationPrincipal LoginMember loginMember){
        ItemInfoRespDto itemInfoRespDto = itemService.showItemInfo(itemId);
        return new ResponseEntity<>(ApiUtils.success(itemInfoRespDto),HttpStatus.OK);
    }
    @PostMapping("/items/pick") //아이템 pick 하기 (클라이언트측에서 서버로 pick 여부 전달)
    public ResponseEntity<?> itemPick(@RequestBody ItemPickReqDto itemPickReqDto, HttpServletRequest request, @AuthenticationPrincipal LoginMember loginMember){
        if(itemPickReqDto.getStatus().equals("true")) {//pick 값은 true 아니면 false (String)
            Member memberPS = memberService.findById(loginMember.getUsername());
            Item itemPS = itemService.findById(itemPickReqDto.getItemId());
            pickService.save(Pick.create(memberPS, itemPS));
        }
       return new ResponseEntity<>(ApiUtils.success("대외활동 pick 완료"), HttpStatus.CREATED);
    }

    @GetMapping("/items/{itemId}/rooms") //해당 아이템에 생성된 방들 불러오기
    public ResponseEntity<?> getAllRoom(@PathVariable("itemId") Long itemId,  @AuthenticationPrincipal LoginMember loginMember, HttpServletRequest request){ //ResponseEntity<JSONArray>로 반환형 바꿔서 테스트해보기
        Item findOne = itemService.findById(itemId);
        return new ResponseEntity<>(ApiUtils.success(roomService.findByItemList(findOne)),HttpStatus.OK);
    }
    @GetMapping("items/search")
    public ResponseEntity<?> searchItems(@RequestParam String keyword, @AuthenticationPrincipal LoginMember loginMember) {
        List<SearchDto> firstList = itemService.searchItems(keyword);
        List<SearchDto> secondList = tagService.searchItems(keyword);
        firstList.addAll(secondList);
        return new ResponseEntity<>(ApiUtils.success(firstList),HttpStatus.OK);
    }

    //아이템 검색하기 -> 제목, 상세내용, 태그 등으로 검색
    //키워드 입력 시 대외활동 별로 DB에 저장된 각종 정보와 대조하여 일치하는 정보가 존재할 경우, 해당 대외활동의 제목을 리스트업하고
    // 클릭 시 해당 대외활동의 상세 정보 화면으로 이동
//    @GetMapping("/item/search")
//    public JSONArray itemSearch(ItemSerarchDto dto){ -> 이거 jpql로 풀어서 값이 존재할 때랑 아닐 때로 풀게 할 지 고민해봐야할듯 아니면 queryDSL로 풀어서 할 지???
//    }


    //=============================================

    /*
    {
  "Question" : {
	"1" : "이름은?"
	"2" :  "나이는?"
   },
  "Room" : {
	"title" : "방 제목",
	"content" : "방 소개글",
	"city" : "제주",
	"capacity" : "2명",
	"memberId" : "팀장 아이디" -> 헤더로 넘길 것? 그런데 보안에 크게 영향 없어서 그냥 바디로 받는 것도 ㄱㅊ
	"itemId" : "아이템 아이디" -> 헤더로 넘길 것
     }
    }
     */
    @PostMapping("/items/{itemId}/rooms") //해당 아이템에 방 생성하기
    public ResponseEntity<?> createRoom(@PathVariable("itemId")Long itemId, @RequestBody MakeRoomReqDto makeRoomReqDto, @AuthenticationPrincipal LoginMember loginMember) throws JsonProcessingException {
        List<Question> questionsPS = questionService.makeQuestion(makeRoomReqDto);
        MakeRoomRespDto makeRoomRespDto = roomService.makeRoom(itemId, makeRoomReqDto, loginMember.getMember(), questionsPS);

        return new ResponseEntity<>(ApiUtils.success(makeRoomRespDto),HttpStatus.CREATED);
    }

    @GetMapping("/items/{itemId}/rooms/{roomId}/applications") //해당 아이템에 지원하기 위해 설문 양식 질문 보기
    public ResponseEntity<?> viewApplicationForm(@PathVariable("itemId")Long itemId, @PathVariable("roomId") Long roomId, @AuthenticationPrincipal LoginMember loginMember){
        Room roomPS = roomService.findByRoomIdAndItemId(roomId, itemId);
        QuestionMapDto questions = questionService.findQuestions(roomPS.getSurvey());
        return new ResponseEntity<>(ApiUtils.success(questions),HttpStatus.OK);
    }
    /*
{
    "질문1 아이디" : "answer1",
    "질문2 아이디" : "answer2"
}
 */
    //TODO 테스트해야함

    @PostMapping("/items/{itemId}/rooms/{roomId}/applications") //설문 답변 달고 지원 완료하기
    public ResponseEntity<?> applyComplete(@PathVariable("itemId")Long itemId, @PathVariable("roomId")Long roomId, @RequestBody QuestionAnswerListDto questionAnswerListDto, @AuthenticationPrincipal LoginMember loginMember) throws JsonProcessingException {

        Room findRoom = roomService.findByRoomIdAndItemId(roomId, itemId);
        Member findMember = loginMember.getMember();

        if(findRoom.getMember() == findMember) return ResponseEntity.badRequest().body("직접 생성한 방에는 지원할 수 없습니다.");

        surveyAnswerService.applyRoom(questionAnswerListDto, findRoom, findMember);

        return new ResponseEntity<>(ApiUtils.success("success"), HttpStatus.OK);
    }

}
