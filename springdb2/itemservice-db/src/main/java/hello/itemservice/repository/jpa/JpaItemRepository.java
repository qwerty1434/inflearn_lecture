package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Transactional // JPA안의 모든 데이터 변경은 트랜잭션 안에서 일어나야 합니다.
public class JpaItemRepository implements ItemRepository {
    private final EntityManager em; // 엔티티 매니저를 주입바당야 합니다.

    public JpaItemRepository(EntityManager em){
        this.em = em;
    }

    @Override
    public Item save(Item item) {
        em.persist(item);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = em.find(Item.class, itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
        // 다시 save할 필요 없음 -> dirty checking
    }

    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class,id); // 변수로 타입,pk를 넣어주면 됩니다.
        return Optional.ofNullable(item);
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        // findAll은 jpql을 사용함
        // jpql: 객체 쿼리, 테이블이 아닌 엔티티를 대상으로 쿼리를 작성함
        // jpql도 동적쿼리 작성이 어렵다는 단점이 있음
        String jpql = "select i from Item i";
        List<Item> result = em.createQuery(jpql, Item.class).getResultList();
        return null;
    }
}
