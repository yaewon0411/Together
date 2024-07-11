package together.capstone2together.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.item.Item;
import together.capstone2together.domain.room.Room;
import together.capstone2together.domain.room.RoomRepository;
import together.capstone2together.domain.item.ItemRepository;
import together.capstone2together.dto.item.ItemReqDto;
import together.capstone2together.ex.CustomApiException;
import together.capstone2together.util.CustomDateUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static together.capstone2together.dto.item.ItemReqDto.*;
import static together.capstone2together.dto.item.ItemRespDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private volatile List<Top20ViewsRespDto> cachedTopViews = new ArrayList<>();
    private final ItemRepository itemRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public Item save(ItemReqDto itemReqDto){
        validateDuplicatedItem(itemReqDto.toEntity());
        return itemRepository.save(Item.create(itemReqDto));
    }

    private void validateDuplicatedItem(Item item) {
        if(itemRepository.findByTitleAndDeadline(item.getTitle(), item.getDeadline()).isPresent())
            throw new CustomApiException("이미 존재하는 대외활동 입니다.");
    }
    @Scheduled(cron = "0 0 */1 * * ?")
    public void updateTopViewCache(){
        List<Item> items = itemRepository.findTop20ByViews(CustomDateUtil.getCurrentTime());
        cachedTopViews = items.stream()
                .map(Top20ViewsRespDto::new)
                .collect(Collectors.toList());
        System.out.println("인기 대외 활동 업데이트 : " + LocalDateTime.now());
    }

    //실시간 인기 활동
    public List<Top20ViewsRespDto> getTop20Views(){
        return new ArrayList<>(cachedTopViews);
    }

    public Item findById(Long id){
        return itemRepository.findById(id).get();
    }


    @Async
    @Transactional
    public void increaseViewAsync(Long itemId){
        Item itemPS = itemRepository.findById(itemId).orElseThrow(
                () -> new CustomApiException("해당 대외활동은 존재하지 않습니다")
        );
        itemPS.increaseView();
    }

    @Transactional
    public ItemInfoRespDto showItemInfo(Long itemId){
        //아이템 찾기
        Item itemPS = itemRepository.findById(itemId).orElseThrow(
                () -> new CustomApiException("해당 대외활동은 존재하지 않습니다")
        );

        //비동기로 조회수 증가
        increaseViewAsync(itemId);

        return new ItemInfoRespDto(itemPS);
    }



    //마감 직전 활동
    public List<ImminentDeadlineRespDto> getImminentDeadline(){
        List<Item> findList = itemRepository.findTop20ByDeadlineAfterOrderByDeadlineAsc(CustomDateUtil.getCurrentTime());
        List<ImminentDeadlineRespDto> response = new ArrayList<>();
        findList.forEach(
                item -> {
                    Optional<Room> roomOP = roomRepository.findByItem(item);
                    roomOP.ifPresent(room -> response.add(new ImminentDeadlineRespDto(room, item)));
                }
        );
        return response;
    }


    //최근 추가된 활동
    public List<RecentlyAddRespDto> getRecentlyAddedItem(){
        List<Item> findList = itemRepository.findTop20ByOrderByIdDesc(CustomDateUtil.getCurrentTime());
        List<RecentlyAddRespDto> response = new ArrayList<>();
        findList.stream().forEach(
                item -> {
                    Optional<Room> roomOP = roomRepository.findByItem(item);
                    roomOP.ifPresent(room -> response.add(new RecentlyAddRespDto(room, item)));
                }
        );
        return response;
    }

    public List<SearchDto> searchItems(String keyword) { //키워드는 제목에 들어있을 수도, 상세 내용에 들어있을 수도, 태그가 될 수도 있음
        List<Item> findList = itemRepository.searchedItem(keyword);
        List<SearchDto> searchList = new ArrayList<>();

        findList.forEach(
                item -> searchList.add(new SearchDto(item))
        );

        return searchList;
    }

}
