package together.capstone2together.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import together.capstone2together.domain.*;
import together.capstone2together.dto.SearchDto;
import together.capstone2together.repository.TagRepository;
import together.capstone2together.service.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home") // 홈 탭
public class HomeController {

    private final ItemService itemService;
    private final ItemTagService itemTagService;
    private final MemberService memberService;
    private final RoomService roomService;
    private final SurveyAnswerService surveyAnswerService;
    private final SurveyService surveyService;
    private final QuestionService questionService;
    private final PickService pickService;
    private final MemberTagService memberTagService;
    private final TagService tagService;

    @GetMapping
    public JSONObject findItemByInterestedTag(HttpServletRequest request) {
        String memberId = request.getHeader("memberId");
        Member findOne = memberService.findById(memberId);
        List<MemberTag> tagList = findOne.getTagList();

        JSONObject result = new JSONObject();

        result.put("실시간 인기 활동", itemService.getTop20Views());
        result.put("마감 직전 활동", itemService.getImminentDeadline());
        result.put("내가 관심있는 활동", itemTagService.findItemByInterestedTag(findOne.getTagList()));
        result.put("최근 추가된 활동", itemService.getRecentlyAddedItem());

        return result;
    }

    @GetMapping("/item") //홈 화면에서 특정 아이템 누르면 그 아이템의 상세 정보가 나오도록
    public JSONObject getItemInfo(HttpServletRequest request){
        Long itemId = Long.valueOf(request.getHeader("itemId"));
        return itemService.showItemInfo(itemId);
    }
    @PostMapping("/item/pick") //아이템 pick 하기 (클라이언트측에서 서버로 pick 여부 전달)
    public ResponseEntity<String> itemPick(HttpServletRequest request){
        String status = request.getHeader("pick"); //pick 값은 true 아니면 false (String)
        if(request.getHeader("pick")!=null){
            String memberId = request.getHeader("memberId");
            Long itemId = Long.valueOf(request.getHeader("itemId"));
            Pick pick = Pick.create(memberService.findById(memberId),itemService.findById(itemId));
            pickService.save(pick);
        }
        return ResponseEntity.ok("success");
    }

    @GetMapping("/item/room") //해당 아이템에 생성된 방들 불러오기
    public JSONArray getAllRoom(HttpServletRequest request){ //ResponseEntity<JSONArray>로 반환형 바꿔서 테스트해보기
        JSONObject object = new JSONObject();
        Long itemId = Long.valueOf(request.getHeader("itemId"));
        Item findOne = itemService.findById(itemId);
        return roomService.findByItem(findOne);
    }

    //아 이거 좀 수정있음......기획이라고 검색하면 대외활동도 같이 묶여서 나옴. 해겷가ㅣ
    @GetMapping("/search")
    public ResponseEntity<List<SearchDto>> searchItems(@RequestParam String keyword) {
        List<SearchDto> firstList = itemService.searchItems(keyword);
        //List<SearchDto> secondList = itemTagService.searchItems(keyword);
        List<SearchDto> secondList = tagService.searchItems(keyword);
        firstList.addAll(secondList);
        return ResponseEntity.ok(firstList);
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
    @PostMapping("/item/room/make") //해당 아이템에 방 생성하기
    public ResponseEntity<String> makeRoom(@RequestBody String jsonString, HttpServletRequest request) throws JsonProcessingException {

        String memberId1 = request.getHeader("memberId");
        if(memberId1==null) throw new IllegalStateException("로그인 해주세요");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        Question question = questionService.makeQuestion(jsonNode.get("Question"));
        Survey findSurvey = surveyService.save(new Survey(question));
        JsonNode roomNode = jsonNode.get("Room");
        Long itemId = roomNode.findValue("itemId").asLong();
        String memberId = roomNode.findValue("memberId").asText();

        Item findItem = itemService.findById(itemId);
        Member findMember = memberService.findById(memberId);
        Room room = roomService.makeRoom(findItem, findMember, findSurvey, roomNode);
        roomService.save(room);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/item/room/apply") //해당 아이템에 지원하기 위해 설문 양식 질문 보기
    public JSONObject applyRoom(HttpServletRequest request){
        Long roomId = Long.valueOf(request.getHeader("roomId"));
        Room findOne = roomService.findById(roomId);
        return questionService.showQuestionList(findOne.getSurvey().getQuestion());
    }
    /*
{
    "1" : "answer1",
    "2" : "answer2"
}
 */
    @PostMapping("/item/room/apply/complete") //설문 답변 달고 지원 완료하기
    public ResponseEntity<String> applyComplete(@RequestBody String jsonString, HttpServletRequest request) throws JsonProcessingException {

        Long roomId = Long.valueOf(request.getHeader("roomId"));
        String memberId = request.getHeader("memberId");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        List<String> answerList = new ArrayList<>();

        for (JsonNode node : jsonNode) {
            answerList.add(node.textValue());
        }
        System.out.println("answerList = " + answerList);

        Room findRoom = roomService.findById(roomId);
        Member findMember = memberService.findById(memberId);
        Question findQuestion = findRoom.getSurvey().getQuestion();

        SurveyAnswer surveyAnswer = SurveyAnswer.create(findRoom, findMember, findQuestion, answerList);
        surveyAnswerService.save(surveyAnswer);
        return ResponseEntity.ok("success");
    }

}
