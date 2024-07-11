package together.capstone2together.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import together.capstone2together.domain.item.Item;
import together.capstone2together.domain.member.Member;
import together.capstone2together.service.MemberService;
import together.capstone2together.service.*;
import together.capstone2together.util.ApiUtils;

import static together.capstone2together.dto.interest.InterestReqDto.*;
import static together.capstone2together.dto.item.ItemRespDto.*;

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
    @GetMapping("/{memberId}")
    public ResponseEntity<?> showItemByAi(@PathVariable("memberId")String memberId){
        Member findOne = memberService.findById(memberId);
        Item findItem = aiService.getOne(findOne);
        ItemInfoRespDto itemInfoRespDto = itemService.showItemInfo(findItem.getId());
        return new ResponseEntity<>(ApiUtils.success(itemInfoRespDto), HttpStatus.OK);
    }
    @PostMapping("/interest") // ai가 추천한 아이템에 표기한 관심도 -> 관심도>=4 이면 사용자 pick에 추가
    public ResponseEntity<?> getInterest(AddInterestToPickReqDto addInterestToPickReqDto){

        interestService.addInterestToPick(addInterestToPickReqDto); //TODO: 이 메서드 안에서 해결하도록 수정중
        return new ResponseEntity<>(ApiUtils.success("AI 추천 아이템 관심도 표기 완료"),HttpStatus.OK);

//        int score = Integer.parseInt(request.getHeader("interest"));
//        if(score>0){
//            Member findMember = memberService.findById(request.getHeader("memberId"));
//            Item findItem = itemService.findById(Long.valueOf(request.getHeader("itemId")));
//            //이미 해당 아이템에 대해 표기한 관심사가 있으면 지울것
//            interestService.ifPresentPreviousRecordThenDelete(findMember, findItem);
//            if(score>3) {
//                if(!pickService.findByMemberAndItem(findMember, findItem)) {
//                    Pick pick = Pick.create(findMember, findItem);
//                    pickService.save(pick);
//                }
//            }
//            Interest interest = Interest.create(findMember, findItem, score);
//            interestService.save(interest);
//            return ResponseEntity.ok("success");
//        }
//        return ResponseEntity.ok("success-no score");
    }

}
