package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    /**
    * JPA에서 엔티티 매니저(Entity Manager)는 엔티티를 저장하고, 수정하고, 삭제하고 조회하는 등 엔티티와 관련된 모든 일을 처리한다
    */
    private final EntityManager em;

    //상품저장
    public void save(Item item) {
        if(item.getId() == null){ //신규 아이템, 저장 전
            em.persist(item);
        } else { //이미 존재하는 아이템
            em.merge((item));
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

}
