package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속관계 전략, 우리는 singleTable전략을 사용합니다.
@DiscriminatorColumn(name="dtype")
@Getter @Setter
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//
    // DDD관점에서 보면 엔티티 자체가 해결할 수 있는 건 엔티티 안에서 해결하는 게 좋습니다.
    // 실무에서는 Setter가 아니라 꼭 아래와 같은 메서드로 값을 변경해줘야 합니다.
    // 재고 증가
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    // 재고 감소
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
