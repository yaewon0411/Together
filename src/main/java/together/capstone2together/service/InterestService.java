package together.capstone2together.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.interest.Interest;
import together.capstone2together.domain.item.Item;
import together.capstone2together.domain.item.ItemRepository;
import together.capstone2together.domain.member.Member;
import together.capstone2together.domain.member.MemberRepository;
import together.capstone2together.domain.pick.Pick;
import together.capstone2together.ex.CustomApiException;
import together.capstone2together.domain.interest.InterestRepository;
import together.capstone2together.domain.pick.PickRepository;

import java.util.Optional;

import static together.capstone2together.dto.interest.InterestReqDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterestService {
    private final InterestRepository interestRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final PickRepository pickRepository;

    @Transactional
    public void save (Interest interest){
        interestRepository.save(interest);
    }

    @Transactional
    public void ifPresentPreviousRecordThenDelete(Member member, Item item){
        Optional<Interest> findOne = interestRepository.findByMemberAndItem(member, item);
        findOne.ifPresent(interestRepository::delete);
    }

    //TODO - 테스트 해야 함
    //ai가 추천한 아이템에 회원이 표기한 관심도가 -> 관심도>=4 이면 사용자 pick에 추가!!
    @Transactional
    public void addInterestToPick(AddInterestToPickReqDto addInterestToPickReqDto) {
        //회원 찾기
        Member memberPS = memberRepository.findById(addInterestToPickReqDto.getMemberId()).orElseThrow(
                () -> new CustomApiException("존재하지 않는 회원입니다")
        );
        //아이템(대외활동 찾기)
        Item itemPS = itemRepository.findById(addInterestToPickReqDto.getItemId()).orElseThrow(
                () -> new CustomApiException("존재하지 않는 대외활동입니다")
        );

        //interest에 이미 있으면 -> 수정, 없으면 -> 생성
        Interest interestPS = interestRepository.findByMemberAndItem(memberPS, itemPS).
                orElse(interestRepository.save(Interest.create(memberPS, itemPS, addInterestToPickReqDto.getScore())));

        Optional<Interest> interestOP = interestRepository.findByMemberAndItem(memberPS, itemPS);
        if(interestOP.isEmpty())
            interestPS = interestRepository.save(Interest.create(memberPS, itemPS, addInterestToPickReqDto.getScore()));
        else{
            //이전에 4점 이상으로 부여됐었는데, 새롭게 부과된 점수가 3점 이하인 경우 pick에서 삭제해야 함
            if(interestPS.getScore()<4) {
                interestPS.changeScore(addInterestToPickReqDto.getScore());
                interestRepository.delete(interestPS);
                return;
            }
        }
        //score>=4 이면 pick에 추가
        if(interestPS.getScore()>=4){
            pickRepository.save(Pick.create(memberPS, itemPS));
        }
    }

}
