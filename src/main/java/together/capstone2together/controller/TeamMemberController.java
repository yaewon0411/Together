package together.capstone2together.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import together.capstone2together.config.auth.LoginMember;
import together.capstone2together.domain.*;
import together.capstone2together.domain.member.Member;
import together.capstone2together.dto.room.RoomReqDto;
import together.capstone2together.dto.surveyAnswer.SurveyAnswerReqDto;
import together.capstone2together.dto.surveyAnswer.SurveyAnswerReqDto.SurveyStatusReqDto;
import together.capstone2together.service.MemberService;
import together.capstone2together.domain.room.Room;
import together.capstone2together.service.RoomService;
import together.capstone2together.service.*;
import together.capstone2together.util.ApiUtils;


import java.util.List;

import static together.capstone2together.dto.room.RoomReqDto.*;
import static together.capstone2together.service.RoomService.*;
import static together.capstone2together.dto.room.RoomRespDto.*;
import static together.capstone2together.dto.surveyAnswer.SurveyAnswerRespDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team-members") //팀원 모집 화면
public class TeamMemberController {

    private final RoomMemberService roomMemberService;
    private final AnswerService answerService;

    private final MemberService memberService;
    private final RoomService roomService;
    private final SurveyAnswerService surveyAnswerService;

    private final SurveyService surveyService;
    private final QuestionService questionService;

    //==========================팀장탭=====================================

    //팀장 탭 - 팀장이 생성한 방 리스트
    @GetMapping("/rooms/created")
    public ResponseEntity<?> showCreatedRoom(@AuthenticationPrincipal LoginMember loginMember) {
        List<CreatedRoomDto> creatorRoomList = roomService.findCreatorRoomList(loginMember.getMember());
        return new ResponseEntity<>(ApiUtils.success(creatorRoomList), HttpStatus.OK);
    }

    //팀장 탭 - 팀장이 생성한 방에 모든 지원자가 들어온 경우 역할과 함께 뿌림
    @GetMapping("/rooms/{roomId}/applicants")
    public ResponseEntity<?> showAll(@PathVariable("roomId")Long roomId, @AuthenticationPrincipal LoginMember loginMember){ //Long은 RequestParam으로 안돼서 post로 맵핑

        Room roomPS = roomService.findById(roomId);
        if(roomPS.isFull()) {
            ShowJoinedMemberRespDto showJoinedMemberRespDto = roomService.showJoinedMember(roomPS, loginMember.getMember());
            return new ResponseEntity<>(ApiUtils.success(showJoinedMemberRespDto),HttpStatus.OK);
        }
        AppliedMemberRespDto appliedMemberList = surveyAnswerService.getAppliedMemberList(roomPS);
        return new ResponseEntity<>(ApiUtils.success(appliedMemberList),HttpStatus.OK);
    }

    //TODO - 테스트!!!
    @DeleteMapping("/rooms/{roomId}") //지원자가 0명인 경우 방 삭제 가능
    public ResponseEntity<?> deleteRoom(@PathVariable("roomId")Long roomId, @AuthenticationPrincipal LoginMember loginMember){
        roomService.deleteRoom(roomId, loginMember.getMember());
        return new ResponseEntity<>(ApiUtils.success("방 삭제 완료"),HttpStatus.OK);
    }

    //이게 어떤 지원자를 눌렀는 지는 showAll에서 넘긴 지원자 아이디로 검색
    @GetMapping("/rooms/{roomId}/applicants/{memberId}/survey-answers") //팀장 탭 - 지원자 눌렀을 때 작성한 설문 답변 보기
    public ResponseEntity<?> getSurveyAnswer(@PathVariable("roomId")Long roomId,@PathVariable("memberId")String memberId, @AuthenticationPrincipal LoginMember loginMember){
        List<SurveyAnswerReplyRespDto> respDto
                = surveyAnswerService.findByMemberId(memberId, roomId);//지원자 아이디
        return new ResponseEntity<>(ApiUtils.success(respDto),HttpStatus.OK);
    }
    @PatchMapping("/survey-answers/{surveyAnswerId}/status") //설문 답변 pass or fail
    public ResponseEntity<?> surveyPass(@PathVariable("surveyAnswerId")Long surveyAnswerId, @RequestBody SurveyStatusReqDto surveyStatusReqDto, @AuthenticationPrincipal LoginMember loginMember){ //팀장 탭 - 설문 답변 pass 판정 시키기
        Object response = surveyAnswerService.surveyAnswerStatus(surveyAnswerId, loginMember.getMember(), surveyStatusReqDto);
        return new ResponseEntity<>(ApiUtils.success(response),HttpStatus.OK);
    }

    //=====================================팀원 탭===================================================
    @GetMapping("/members/login-member/applicants")
    public ResponseEntity<?> showJoinedRoom(@AuthenticationPrincipal LoginMember loginMember){ //팀원 탭 - 내가 지원한 방 목록들 보기
        Member memberPS = memberService.findById(loginMember.getMember().getId());
        List<JoinedMemberRespDto> joinedRoomList = surveyAnswerService.getJoinedRoomList(memberPS);
        return new ResponseEntity<>(ApiUtils.success(joinedRoomList),HttpStatus.OK);
    }
    @GetMapping("/rooms/{roomId}/participants") //팀원 탭 - 방에 인원이 다 차면 참여한 멤버들의 이름과 직책, 연락처를 리스트업. 안 차면 팀원 입장에서는 아무것도 안보이게?? 이거 물어보기
    public ResponseEntity<?> showAllByJoinedMember(@PathVariable("roomId")Long roomId, @AuthenticationPrincipal LoginMember loginMember){
        Room roomPS = roomMemberService.trueJoinedMember(loginMember.getMember(), roomId);
        AppliedMemberRespDto appliedMemberList = surveyAnswerService.getAppliedMemberList(roomPS);
        return new ResponseEntity<>(ApiUtils.success(appliedMemberList),HttpStatus.OK);
    }

}
