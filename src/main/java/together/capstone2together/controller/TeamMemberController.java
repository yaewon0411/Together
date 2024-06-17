package together.capstone2together.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import together.capstone2together.config.auth.LoginMember;
import together.capstone2together.domain.*;
import together.capstone2together.domain.member.Member;
import together.capstone2together.dto.room.RoomReqDto;
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
@RequestMapping("/team-member") //팀원 모집 화면
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
    @GetMapping("/creator")
    public ResponseEntity<?> showCreatedRoom(@AuthenticationPrincipal LoginMember loginMember) {
        List<CreatedRoomDto> creatorRoomList = roomService.findCreatorRoomList(loginMember.getMember());
        return new ResponseEntity<>(ApiUtils.success(creatorRoomList), HttpStatus.OK);
    }

    //팀장 탭 - 팀장이 생성한 방에 모든 지원자가 들어온 경우 역할과 함께 뿌림
    @GetMapping("/creator/showJoined")
    public ResponseEntity<?> showAll(HttpServletRequest request, @AuthenticationPrincipal LoginMember loginMember){ //Long은 RequestParam으로 안돼서 post로 맵핑

        Room roomPS = roomService.findById(Long.valueOf(request.getHeader("roomId")));
        if(roomPS.isFull()) {
            ShowJoinedMemberRespDto showJoinedMemberRespDto = roomService.showJoinedMember(roomPS, loginMember.getMember());
            return new ResponseEntity<>(ApiUtils.success(showJoinedMemberRespDto),HttpStatus.OK);
        }
        AppliedMemberRespDto appliedMemberList = surveyAnswerService.getAppliedMemberList(roomPS);
        return new ResponseEntity<>(ApiUtils.success(appliedMemberList),HttpStatus.OK);
    }

    //TODO - 테스트!!!
    @DeleteMapping("/creator/delete") //지원자가 0명인 경우 방 삭제 가능
    public ResponseEntity<?> deleteRoom(HttpServletRequest request, @AuthenticationPrincipal LoginMember loginMember){
        roomService.deleteRoom(Long.valueOf(request.getHeader("roomId")), loginMember.getMember());
        return new ResponseEntity<>(ApiUtils.success("방 삭제 완료"),HttpStatus.OK);
    }

    //이게 어떤 지원자를 눌렀는 지는 showAll에서 넘긴 지원자 아이디로 검색
    @GetMapping("/creator/showJoined/surveyAnswer") //팀장 탭 - 지원자 눌렀을 때 작성한 설문 답변 보기
    public ResponseEntity<?> getSurveyAnswer(HttpServletRequest request, @AuthenticationPrincipal LoginMember loginMember){
        List<SurveyAnswerReplyRespDto> respDto
                = surveyAnswerService.findByMemberId(request.getHeader("memberId"), Long.valueOf(request.getHeader("roomId")));//지원자 아이디
        return new ResponseEntity<>(ApiUtils.success(respDto),HttpStatus.OK);
    }
    @PatchMapping("/creator/showJoined/surveyAnswer/pass")
    public ResponseEntity<?> surveyPass(HttpServletRequest request, @AuthenticationPrincipal LoginMember loginMember){ //팀장 탭 - 설문 답변 pass 판정 시키기
        MakeAnswerToPassRespDto makeAnswerToPassRespDto
                = surveyAnswerService.makeAnswerToPass(Long.valueOf(request.getHeader("surveyAnswerId")), loginMember.getMember());
        return new ResponseEntity<>(ApiUtils.success(makeAnswerToPassRespDto),HttpStatus.OK);
    }
    @PatchMapping("/creator/showJoined/surveyAnswer/fail")
    public ResponseEntity<?> surveyFail(HttpServletRequest request, @AuthenticationPrincipal LoginMember loginMember){ //팀장 탭 - 설문 답변 fail 판정 시키기
        surveyAnswerService.makeAnswerToFail(Long.valueOf(request.getHeader("surveyAnswerId")), loginMember.getMember());
        return new ResponseEntity<>(ApiUtils.success("답변 Fail 판정 완료"),HttpStatus.OK);
    }

    //=====================================팀원 탭===================================================
    @GetMapping("/member")
    public ResponseEntity<?> showJoinedRoom(HttpServletRequest request){ //팀원 탭 - 내가 지원한 방 목록들 보기
        Member memberPS = memberService.findById(request.getHeader("memberId"));
        List<JoinedMemberRespDto> joinedRoomList = surveyAnswerService.getJoinedRoomList(memberPS);
        return new ResponseEntity<>(ApiUtils.success(joinedRoomList),HttpStatus.OK);
    }
    @GetMapping("/member/showJoined") //팀원 탭 - 방에 인원이 다 차면 참여한 멤버들의 이름과 직책, 연락처를 리스트업. 안 차면 팀원 입장에서는 아무것도 안보이게?? 이거 물어보기
    public ResponseEntity<?> showAllByJoinedMember(HttpServletRequest request, @AuthenticationPrincipal LoginMember loginMember){
        Room roomPS = roomMemberService.trueJoinedMember(loginMember.getMember(), Long.valueOf(request.getHeader("roomId")));
        AppliedMemberRespDto appliedMemberList = surveyAnswerService.getAppliedMemberList(roomPS);
        return new ResponseEntity<>(ApiUtils.success(appliedMemberList),HttpStatus.OK);
    }

}
