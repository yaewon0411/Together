package together.capstone2together.service;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.Item;
import together.capstone2together.domain.Member;
import together.capstone2together.domain.Pick;
import together.capstone2together.repository.PickRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PickService {
    private final PickRepository pickRepository;
    private final SubService subService;

    @Transactional
    //찜 저장
    public void save (Pick pick){
        validateDuplicatedPick(pick);
        pickRepository.save(pick);
    }
    //이미 찜한 아이템인지 중복 검사
    private void validateDuplicatedPick(Pick pick) {
        List<Pick> findList = pickRepository.findByMemberAndItem(pick.getMember(), pick.getItem());
        for (Pick pick1 : findList) {
            System.out.println("pick = " + pick1.getItem().getId() + pick1.getMember().getName());
        }
        if(findList.size()>0) throw new IllegalStateException("이미 찜한 대외활동 입니다.");
    }
    public JSONArray findByMember(Member member){
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        List<Item> findList = pickRepository.findByMember(member);
        for (Item item : findList) {
            array.add(subService.makeObject(item, object));
        }
        return array;
    }

}
