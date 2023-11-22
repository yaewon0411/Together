package together.capstone2together.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import together.capstone2together.domain.Member;
import together.capstone2together.domain.MemberTag;
import together.capstone2together.domain.Tag;
import together.capstone2together.dto.JoinDto;
import together.capstone2together.dto.LoginDto;
import together.capstone2together.service.MemberService;
import together.capstone2together.service.MemberTagService;
import together.capstone2together.service.TagService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final MemberTagService memberTagService;
    private final TagService tagService;

    @PostMapping("/join") //회원가입
    public ResponseEntity<String> join(@RequestBody JoinDto dto) {
        Member member = new Member(dto.getId(), dto.getPassword(), dto.getName(), dto.getKakaotalkId());
        memberService.join(member);

        List<String> tagList = dto.getTagList();
        for (String name : tagList) {
            Tag findTag = tagService.findOneByName(name);
            if(findTag == null){
                Tag tag = Tag.create(name);
                tagService.save(tag);
                findTag = tagService.findOneByName(tag.getName());
            }
            MemberTag memberTag = MemberTag.create(member,findTag);
            memberTagService.save(memberTag);
        }
        return ResponseEntity.ok("join success");
    }
    @PostMapping("/login") //로그인
    public ResponseEntity<String> login(@RequestBody LoginDto dto){
        memberService.login(dto.getId(), dto.getPassword());
        return ResponseEntity.ok("login success");
    }

}
