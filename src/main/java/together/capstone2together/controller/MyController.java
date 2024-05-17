package together.capstone2together.controller;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import together.capstone2together.domain.*;
import together.capstone2together.dto.ChangePwDto;
import together.capstone2together.dto.TagListDto;
import together.capstone2together.domain.member.Member;
import together.capstone2together.domain.member.MemberService;
import together.capstone2together.dto.member.MemberReqDto;
import together.capstone2together.dto.member.MemberRespDto;
import together.capstone2together.dto.member.MemberRespDto.ChangeKakaotalkIdRespDto;
import together.capstone2together.service.*;
import together.capstone2together.util.ApiUtils;

import java.util.List;

import static together.capstone2together.dto.member.MemberReqDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my") // 마이페이지 탭
public class MyController {

    private final MemberService memberService;
    private final SurveyAnswerService surveyAnswerService;
    private final MemberTagService memberTagService;
    private final TagService tagService;
    private final PickService pickService;


    //포인트 조회, 개설한 방 수, 지원한 방 수(waiting, pass), 닉네임(name), 아이디 내보내기
    @GetMapping
    public ResponseEntity<JSONObject> myInfo(HttpServletRequest request){
        String memberId = request.getHeader("memberId");
        Member findMember = memberService.findById(memberId);
        List<SurveyAnswer> findAnswerList = surveyAnswerService.findByMemberExcludeFailSurvey(findMember);

        int point = findMember.getPoint();
        String name = findMember.getName();
        int createdRoomCnt = findMember.getLedRooms().size();
        int applyRoomCnt = findAnswerList.size();

        JSONObject object = new JSONObject();
        object.put("point", point);
        object.put("name", name);
        object.put("createdRoomCnt", createdRoomCnt);
        object.put("applyRoomCnt", applyRoomCnt);

        return ResponseEntity.ok(object);
    }
    /*
    태그 리스트 재설정
        {"tags": ["기술", "예술", "여행"]}
     */
    @PostMapping("/tags")
    public ResponseEntity<String> changeInterestedTag(@RequestBody TagListDto dto, HttpServletRequest request){

        String memberId = request.getHeader("memberId");
        Member findMember = memberService.findById(memberId);

        List<String> tagNameList = dto.getTagList();
        for (String name : tagNameList) {
            Tag tag = tagService.findOneByName(name);
            memberTagService.changeTags(findMember, tag);
        }
       return ResponseEntity.ok("success");
    }
    //비밀번호 재설정
    @PostMapping("/pw")
    public ResponseEntity<String> changePw(HttpServletRequest request, @RequestBody ChangePwDto dto){
        String memberId = request.getHeader("memberId");
        memberService.changePw(memberService.findById(memberId), dto.getChangePw());
        return ResponseEntity.ok("비밀번호 재설정 완료");
    }
    @PostMapping("/kakaotalkId")
    public ResponseEntity<?> changeKakaotalkId(HttpServletRequest request, @RequestBody ChangeKakaotalkIdReqDto changeKakaotalkIdDto){
        Member findOne = memberService.findById(request.getHeader("memberId"));
        ChangeKakaotalkIdRespDto changeKakaotalkIdRespDto = memberService.changeKakaotalkId(findOne, changeKakaotalkIdDto);
        return new ResponseEntity<>(ApiUtils.success(changeKakaotalkIdRespDto),HttpStatus.OK);
    }

    //관심있는 활동 -> pick 한 아이템들 리스트로 내보내기
    @GetMapping("/pick")
    public ResponseEntity<?> getPickItems(HttpServletRequest request){
        Member findOne = memberService.findById(request.getHeader("memberId"));
        return new ResponseEntity<>(ApiUtils.success(pickService.getPickItemList(findOne)), HttpStatus.OK);
    }


}
