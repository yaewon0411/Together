package together.capstone2together.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import together.capstone2together.domain.member.Member;
import together.capstone2together.domain.tag.Tag;
import together.capstone2together.dto.memberTag.MemberTagReqDto;
import together.capstone2together.dto.tag.TagReqDto;
import together.capstone2together.service.MemberService;
import together.capstone2together.service.MemberTagService;
import together.capstone2together.service.TagService;
import together.capstone2together.util.ApiUtils;

import java.util.Optional;

import static together.capstone2together.dto.member.MemberReqDto.*;
import static together.capstone2together.dto.member.MemberRespDto.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final MemberTagService memberTagService;
    private final TagService tagService;

    @PostMapping("/join")
    public ResponseEntity<?> join (@RequestBody @Valid JoinReqDto joinReqDto, BindingResult bindingResult){
        // 회원 저장
        JoinRespDto joinRespDto = memberService.join(joinReqDto);
        Member findOne = memberService.findById(joinRespDto.getId());

        //태그 & 회원-태그 저장
        for (String tagName : joinReqDto.getTagList()) {
            Optional<Tag> tag = tagService.save(new TagReqDto(tagName));
            tag.ifPresent(value -> memberTagService
                            .save(MemberTagReqDto.MemberTagSaveReqDto.builder().
                                    member(findOne)
                                    .tag(value)
                                    .build()));
        }
        return new ResponseEntity<>(ApiUtils.success(joinRespDto), HttpStatus.CREATED);
    }

}
