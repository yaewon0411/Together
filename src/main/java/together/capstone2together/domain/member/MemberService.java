package together.capstone2together.domain.member;

import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.ex.CustomApiException;
import together.capstone2together.util.ApiUtils;

import java.util.Optional;

import static together.capstone2together.dto.member.MemberReqDto.*;
import static together.capstone2together.dto.member.MemberRespDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService { //예외 처리 서비스 클래스 만들어서 나중에 다 리팩토링

    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    //회원 가입 (TODO- 수정 완료)
    @Transactional
    public JoinRespDto join(JoinReqDto joinReqDto){ //taglist도 다 채워서 넘어와야 되는 거 얘기하기

        //중복 회원 검증
        validateDuplicatedMember(joinReqDto);
        //비밀번호 인코딩 + 저장
        Member member = memberRepository.save(joinReqDto.toEntity(passwordEncoder));

        return new JoinRespDto(member);
    }
    private void validateDuplicatedMember(JoinReqDto joinReqDto) {
        Optional<Member> findOne
                = memberRepository.findById(joinReqDto.getId());
        if(findOne.isPresent()) throw new CustomApiException("이미 존재하는 아이디입니다");
    }
    //로그인
    @Transactional
    public LoginRespDto login(String id, String password){

        if(id == null || password == null) return null;

        Optional<Member> findOne = memberRepository.findById(id);
        if(findOne.isPresent()){
            if(passwordEncoder.matches(password, findOne.get().getPassword())) //인코딩된 비밀번호와 일치하면 true
                return new LoginRespDto(findOne.get());
            else
                throw new CustomApiException("비밀번호가 일치하지 않습니다");
        }
        else
            throw new CustomApiException("존재하지 않는 아이디입니다");
    }

    //포인트 조회
    public int SearchPoint(String id){
        return memberRepository.findById(id).get().getPoint();
    }
    //닉네임 변경
    @Transactional
    public void changeName(String id, String newName){
        memberRepository.updateName(id, newName);
    }
    //비밀번호 변경
    @Transactional
    public void changePw(Member member, String newPassword){
        if (member == null || newPassword == null) {
            throw new IllegalArgumentException("인자가 올바르지 않습니다.");
        }
        member.setPassword(passwordEncoder.encode(newPassword));
    }
    public Member findById(String id){
        Optional<Member> findOne = memberRepository.findById(id);
        if(findOne.isEmpty()) throw new CustomApiException("해당 회원은 존재하지 않습니다.");
        return findOne.get();
    }

    @Transactional
    public ChangeKakaotalkIdRespDto changeKakaotalkId(Member member, ChangeKakaotalkIdReqDto changeKakaotalkIdDto) {
        if(member == null || changeKakaotalkIdDto.getKakaotalkId() == null ){
            throw new CustomApiException("카카오톡 아이디를 변경하기 위한 인자가 올바르지 않습니다.");
        }
        member.setKakaotalkId(changeKakaotalkIdDto.getKakaotalkId());
        return new ChangeKakaotalkIdRespDto("카카오톡 아이디 변경 완료");
    }

}
