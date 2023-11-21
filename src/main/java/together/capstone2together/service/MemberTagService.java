package together.capstone2together.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.Member;
import together.capstone2together.domain.MemberTag;
import together.capstone2together.domain.Tag;
import together.capstone2together.dto.TagListDto;
import together.capstone2together.repository.MemberTagRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberTagService {

    private final MemberTagRepository memberTagRepository;

    public void save(MemberTag memberTag){
        memberTagRepository.save(memberTag);
    }
    //사용자 태그 재설정
    public void changeTags(Member member, Tag tag){
        List<MemberTag> findList = memberTagRepository.findByMember(member);
        for (MemberTag memberTag : findList) {
            memberTagRepository.delete(memberTag);
        }
        MemberTag memberTag = MemberTag.create(member,tag);
        memberTagRepository.save(memberTag);
    }
//    public List<Tag> findItemByInterestedTag(Member member){
//        return memberTagRepository.findItemByInterestedTag(member);
//    }
}

