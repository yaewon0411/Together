package together.capstone2together.Controller;

import com.nimbusds.jose.shaded.json.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import together.capstone2together.domain.Interest;
import together.capstone2together.domain.Item;
import together.capstone2together.domain.Member;
import together.capstone2together.domain.Pick;
import together.capstone2together.service.*;

import java.util.List;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AIController {

    private final MemberService memberService;
    private final ItemService itemService;
    private final AiService aiService;
    private final PickService pickService;
    private final InterestService interestService;

    //해당 유저에게 추천된 아이템 중 랜덤으로 하나 출력 -> ai가 추천한 데이터가 있어야 함. 데이터 없을 시는 어떻게?
    @GetMapping
    public ResponseEntity<JSONObject> showItemByAi(HttpServletRequest request){
        Member findOne = memberService.findById(request.getHeader("memberId"));
        Item findItem = aiService.getOne(findOne);
        return ResponseEntity.ok(itemService.showItemInfo(findItem.getId()));
    }
    @PostMapping("/interest") // ai가 추천한 아이템에 표기한 관심도 -> 관심도>=4 이면 사용자 pick에 추가
    public ResponseEntity<String> getInterest(HttpServletRequest request){
        int score = Integer.parseInt(request.getHeader("interest"));
        if(score>0){
            Member findMember = memberService.findById(request.getHeader("memberId"));
            Item findItem = itemService.findById(Long.valueOf(request.getHeader("itemId")));
            System.out.println("findMember = " + findMember.getName());
            System.out.println("findItem.getId() = " + findItem.getId());
            if(score>3) {
                Pick pick = Pick.create(findMember, findItem);
                pickService.save(pick);
            }
            Interest interest = Interest.create(findMember, findItem, score);
            interestService.save(interest);
            return ResponseEntity.ok("success");
        }
        return ResponseEntity.ok("success-no score");
    }


















}
