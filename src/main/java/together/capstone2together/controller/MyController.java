package together.capstone2together.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import together.capstone2together.config.auth.LoginMember;
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
    public ResponseEntity<?> myInfo(@AuthenticationPrincipal LoginMember loginMember){
        Member findMember = memberService.findById(loginMember.getMember().getId());
        List<SurveyAnswer> findAnswerList = surveyAnswerService.findByMemberExcludeFailSurvey(findMember);

        MemberRespDto.MyInfoRespDto myInfoRespDto = CustomDataUtil.makeMyInfo(findMember, findAnswerList);

        return new ResponseEntity<>(ApiUtils.success(myInfoRespDto),HttpStatus.OK);
    }
    /*
    태그 리스트 재설정
        {"tags": ["기술", "예술", "여행"]}
     */
    @PatchMapping("/tags")
    public ResponseEntity<?> changeInterestedTag(@RequestBody TagListReqDto tagListReqDto, @AuthenticationPrincipal LoginMember loginMember){
        Member findMember = memberService.findById(loginMember.getMember().getId());
        // tag 저장
        tagListReqDto.getTagList().stream().map(tagName -> tagService.save(new TagReqDto(tagName)));
        //memberTag 수정
        memberTagService.changeMemberTags(findMember, tagListReqDto);

       return new ResponseEntity<>(ApiUtils.success("관심 태그 변경 완료"), HttpStatus.OK);
    }

    @PatchMapping("/pw") //비밀번호 재설정
    public ResponseEntity<?> changePw(@AuthenticationPrincipal LoginMember loginMember, @RequestBody ChangePwReqDto changePwReqDto){
        memberService.changePw(memberService.findById(loginMember.getMember().getId()), changePwReqDto);
        return new ResponseEntity<>(ApiUtils.success("비밀번호 변경 완료"),HttpStatus.OK);
    }
    @PatchMapping("/kakaotalkId")
    public ResponseEntity<?> changeKakaotalkId(@AuthenticationPrincipal LoginMember loginMember, @RequestBody ChangeKakaotalkIdReqDto changeKakaotalkIdDto){
        Member findOne = memberService.findById(loginMember.getMember().getId());
        ChangeKakaotalkIdRespDto changeKakaotalkIdRespDto = memberService.changeKakaotalkId(findOne, changeKakaotalkIdDto);
        return new ResponseEntity<>(ApiUtils.success(changeKakaotalkIdRespDto),HttpStatus.OK);
    }

    //관심있는 활동 -> pick 한 아이템들 리스트로 내보내기
    @GetMapping("/picks")
    public ResponseEntity<?> getPickItems(@AuthenticationPrincipal LoginMember loginMember){
        Member findOne = memberService.findById(loginMember.getMember().getId());
        return new ResponseEntity<>(ApiUtils.success(pickService.getPickItemList(findOne)), HttpStatus.OK);
    }


}
