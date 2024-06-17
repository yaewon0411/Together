package together.capstone2together.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import together.capstone2together.domain.item.Item;
import together.capstone2together.domain.room.Room;
import together.capstone2together.domain.room.RoomRepository;
import together.capstone2together.domain.item.ItemRepository;
import together.capstone2together.dto.item.ItemReqDto;
import together.capstone2together.ex.CustomApiException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static together.capstone2together.dto.item.ItemReqDto.*;
import static together.capstone2together.dto.item.ItemRespDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public Item save(Item item){
        validateDuplicatedItem(item);
        return itemRepository.save(item);
    }

    private void validateDuplicatedItem(Item item) {
        if(itemRepository.findByTitleAndDeadline(item.getTitle(), item.getDeadline()).isPresent())
            throw new CustomApiException("이미 존재하는 대외활동 입니다.");
    }

    //실시간 인기 활동
    public List<Top20ViewsRespDto> getTop20Views(){
        List<Item> findList = itemRepository.findTop20ByViews(getCurrentTime());
        List<Top20ViewsRespDto> response = new ArrayList<>();
        findList.stream().forEach(
                item -> {
                    Optional<Room> roomOP = roomRepository.findByItem(item);
                    roomOP.ifPresent(room -> response.add(new Top20ViewsRespDto(room, item)));
                }
        );
        return response;
    }

    public Item findById(Long id){
        return itemRepository.findById(id).get();
    }

    public ItemInfoRespDto showItemInfo(Long id){
        //아이템 찾기
        Item itemPS = itemRepository.findById(id).orElseThrow(
                () -> new CustomApiException("해당 대외활동은 존재하지 않습니다")
        );

        ItemInfoRespDto itemInfoRespDto = new ItemInfoRespDto(itemPS);

        return itemInfoRespDto;
    }



    //마감 직전 활동
    public List<ImminentDeadlineRespDto> getImminentDeadline(){
        List<Item> findList = itemRepository.findTop20ByDeadlineAfterOrderByDeadlineAsc(getCurrentTime());
        List<ImminentDeadlineRespDto> response = new ArrayList<>();
        findList.stream().forEach(
                item -> {
                    Optional<Room> roomOP = roomRepository.findByItem(item);
                    roomOP.ifPresent(room -> response.add(new ImminentDeadlineRespDto(room, item)));
                }
        );
        return response;
    }


    //최근 추가된 활동
    public List<RecentlyAddRespDto> getRecentlyAddedItem(){
        List<Item> findList = itemRepository.findTop20ByOrderByIdDesc(getCurrentTime());
        List<RecentlyAddRespDto> response = new ArrayList<>();
        findList.stream().forEach(
                item -> {
                    Optional<Room> roomOP = roomRepository.findByItem(item);
                    roomOP.ifPresent(room -> response.add(new RecentlyAddRespDto(room, item)));
                }
        );
        return response;
    }
    private static String getCurrentTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentTime.format(formatter);
    }
    public List<SearchDto> searchItems(String keyword) { //키워드는 제목에 들어있을 수도, 상세 내용에 들어있을 수도, 태그가 될 수도 있음
        List<Item> findList = itemRepository.searchedItem(keyword);
        List<SearchDto> searchList = new ArrayList<>();
        for (Item item : findList) {
            SearchDto dto = new SearchDto();
            dto.setId(item.getId());
            dto.setTitle(item.getTitle());
            searchList.add(dto);
        }
        return searchList;
    }

}
