package together.capstone2together.Controller;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import jakarta.persistence.PostLoad;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import together.capstone2together.domain.Member;
import together.capstone2together.domain.MemberTag;
import together.capstone2together.domain.SurveyAnswer;
import together.capstone2together.domain.Tag;
import together.capstone2together.dto.ChangePwDto;
import together.capstone2together.dto.TagListDto;
import together.capstone2together.service.MemberService;
import together.capstone2together.service.MemberTagService;
import together.capstone2together.service.SurveyAnswerService;
import together.capstone2together.service.TagService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my") // 마이페이지 탭
public class MyController {

    private final MemberService memberService;
    private final SurveyAnswerService surveyAnswerService;
    private final MemberTagService memberTagService;

    private final TagService tagService;

    //포인트 조회, 개설한 방 수, 지원한 방 수(waiting, pass), 닉네임(name), 아이디 내보내기
    @GetMapping
    public JSONObject myInfo(HttpServletRequest request){
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

        return object;
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
    public String changePw(HttpServletRequest request, @RequestBody ChangePwDto dto){

        String memberId = request.getHeader("memberId");
        Member findOne = memberService.findById(memberId);
        memberService.changePw(findOne, dto.getChangePw());
        return "비밀번호 재설정 완료";
    }

}
