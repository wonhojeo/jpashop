package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    @Transactional
    public Item updateItem(Long itemId, String name, int price, int stockQuantity){
    /** 엔터티 객체 생성으로 변경감지를 하여 자동으로 수정할 수 있지만, 현실에서 개발을 할 때
        객체 생성은 자제해야 한다. 이렇게 코딩이 되어 있으면 어디서 어떻게 저장되는지, 수정되는지 찾기가 어려워 지기 때문이다.
        따라서 메소드를 만들어서 관리하는 것이 용이하다고 볼 수 있다.

        특히 컨트롤러에서 객체 생성은 지양해야 한다.
    */

        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
        findItem.setId(itemId);

        return findItem;
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }

}
