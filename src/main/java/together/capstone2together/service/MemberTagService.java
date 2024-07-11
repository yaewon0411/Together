package together.capstone2together.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.member.Member;
import together.capstone2together.domain.tag.Tag;
import together.capstone2together.domain.memberTag.MemberTag;
import together.capstone2together.domain.memberTag.MemberTagRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static together.capstone2together.dto.memberTag.MemberTagReqDto.*;
import static together.capstone2together.dto.tag.TagReqDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberTagService {

    private final MemberTagRepository memberTagRepository;

    @Transactional
    public void save(MemberTagSaveReqDto memberTagSaveReqDto){
        if(!validateDuplicatedMemberTage(memberTagSaveReqDto));
            memberTagRepository.save(MemberTag.create(memberTagSaveReqDto.getMember(), memberTagSaveReqDto.getTag()));
    }

    //중복 검사
    public boolean validateDuplicatedMemberTage(MemberTagSaveReqDto memberTagSaveReqDto){
        if(memberTagRepository.existsByMemberAndTag(memberTagSaveReqDto.getMember(), memberTagSaveReqDto.getTag()))
            return true;
        else return false;
    }
    /*
    태그 리스트 재설정
    {"tags": ["기술", "예술", "여행"]}
 */
    @Transactional
    public void changeMemberTags(Member member, TagListReqDto tagListReqDto) {
        //기존 멤버의 memberTag 리스트 조회
        List<MemberTag> existingMemberTags = memberTagRepository.findByMember(member);

        // 기존 태그 이름 추출
        Set<String> existingTagNames = existingMemberTags.stream()
                .map(memberTag -> memberTag.getTag().getName())
                .collect(Collectors.toSet());

        //새 태그 리스트
        Set<String> newTagNames = new HashSet<>(tagListReqDto.getTagList());

        //삭제할 태그 찾기
        Set<MemberTag> tagsToRemove = existingMemberTags.stream()
                .filter(memberTag -> !newTagNames.contains(memberTag.getTag().getName()))
                .collect(Collectors.toSet());

        //삭제
        memberTagRepository.deleteAll(tagsToRemove);

        //추가할 태그 찾기
        Set<String> tagsToAdd = newTagNames.stream()
                .filter(tagName -> !existingTagNames.contains(tagName))
                .collect(Collectors.toSet());

        //새 태그 memebrTag로 등록
        for (String tagName : tagsToAdd) {
            memberTagRepository.save(MemberTag.create(member, new Tag(tagName)));
        }
    }

    public List<MemberTag> findByMember(Member member){

        return memberTagRepository.findByMember(member);
    }
}

