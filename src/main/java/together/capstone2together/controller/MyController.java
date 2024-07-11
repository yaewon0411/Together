package together.capstone2together.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import together.capstone2together.domain.member.Member;
import together.capstone2together.domain.surveyAnswer.SurveyAnswer;
import together.capstone2together.service.MemberService;
import together.capstone2together.service.MemberTagService;
import together.capstone2together.dto.member.MemberRespDto;
import together.capstone2together.dto.member.MemberRespDto.ChangeKakaotalkIdRespDto;
import together.capstone2together.dto.tag.TagReqDto;
import together.capstone2together.service.*;
import together.capstone2together.util.ApiUtils;
import together.capstone2together.util.CustomDataUtil;

import java.util.List;

import static together.capstone2together.dto.member.MemberReqDto.*;
import static together.capstone2together.dto.tag.TagReqDto.*;

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
    public ResponseEntity<?> myInfo(HttpServletRequest request){
        Member findMember = memberService.findById(request.getHeader("memberId"));
        List<SurveyAnswer> findAnswerList = surveyAnswerService.findByMemberExcludeFailSurvey(findMember);

        MemberRespDto.MyInfoRespDto myInfoRespDto = CustomDataUtil.makeMyInfo(findMember, findAnswerList);

        return new ResponseEntity<>(ApiUtils.success(myInfoRespDto),HttpStatus.OK);
    }
    /*
    태그 리스트 재설정
        {"tags": ["기술", "예술", "여행"]}
     */
    @PutMapping("/tags")
    public ResponseEntity<?> changeInterestedTag(@RequestBody TagListReqDto tagListReqDto, HttpServletRequest request){
        Member findMember = memberService.findById(request.getHeader("memberId"));
        // tag 저장
        tagListReqDto.getTagList().stream().map(tagName -> tagService.save(new TagReqDto(tagName)));
        //memberTag 수정
        memberTagService.changeMemberTags(findMember, tagListReqDto);

       return new ResponseEntity<>(ApiUtils.success("관심 태그 변경 완료"), HttpStatus.OK);
    }

    @PostMapping("/pw") //비밀번호 재설정
    public ResponseEntity<?> changePw(HttpServletRequest request, @RequestBody ChangePwReqDto changePwReqDto){
        String memberId = request.getHeader("memberId");
        memberService.changePw(memberService.findById(memberId), changePwReqDto);
        return new ResponseEntity<>(ApiUtils.success("비밀번호 변경 완료"),HttpStatus.OK);
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
