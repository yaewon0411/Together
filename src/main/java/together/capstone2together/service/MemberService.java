package together.capstone2together.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.Member;
import together.capstone2together.dto.TagListDto;
import together.capstone2together.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService { //예외 처리 서비스 클래스 만들어서 나중에 다 리팩토링
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final EntityManager em;

    //회원 가입
    @Transactional
    public void join(Member member){ //taglist도 다 채워서 넘어와야 되는 거 얘기하기
        //비밀번호 인코딩
        String password = member.getPassword();
        String encodePw = passwordEncoder.encode(password);
        member.setPassword(encodePw);
        //중복 회원 검증
        validateDuplicatedMember(member);
        //저장
        memberRepository.save(member);
    }
    private void validateDuplicatedMember(Member member) {
        List<Member> findAll = memberRepository.findAll();
        for (Member findOne : findAll) {
            if(findOne.getId() == member.getId())
                throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }
    //로그인
    @Transactional
    public Member login(String id, String password){

        if(id==null || password==null) return null;

        Optional<Member> findOne = memberRepository.findById(id);
        if(findOne.isPresent()){
            if(passwordEncoder.matches(password, findOne.get().getPassword())) //인코딩된 비밀번호와 일치하면 true
                return findOne.get();
            else //일치 안하면 false
                throw new IllegalStateException("비밀번호가 일치하지 않습니다."); //null로 반환할 지 예외로 반환할 지 얘기해보기
                //return null; //비밀번호가 일치하지 않습니다.
        }
        else 
            throw new NoResultException("존재하지 않는 아이디입니다.");
            //return null; //존재하지 않는 아이디입니다
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
        if(findOne.isEmpty()) throw new NoResultException("해당 회원은 존재하지 않습니다.");
        return findOne.get();
    }

    @Transactional
    public void changeKakaotalkId(Member member, String kakaotalkId) {
        if(member == null || kakaotalkId == null ){
            throw new IllegalArgumentException("인자가 올바르지 않습니다.");
        }
        member.setKakaotalkId(kakaotalkId);
    }

    public boolean findByKakaotalkId(String kakaotalkId) {
        Member member = memberRepository.findByKakaotalkId(kakaotalkId);
        if(member != null) return false;
        else return true;
    }
}
