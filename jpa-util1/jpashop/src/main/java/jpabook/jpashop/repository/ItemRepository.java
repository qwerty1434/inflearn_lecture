package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item){
        // item은 JPA에 저장하기 전까지 id값이 없기 때문에 아래와 같이 설계했습니다.
        if(item.getId() == null){ // item값이 없다는 건 새롭게 생성된 객체라는 의미입니다.
            em.persist(item);
        }else{ // id값이 존재한다는 건 이미 DB에 저장되어 있는 객체라는 의미입니다.
            em.merge(item);
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class,id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i",Item.class)
                .getResultList();
    }
}
