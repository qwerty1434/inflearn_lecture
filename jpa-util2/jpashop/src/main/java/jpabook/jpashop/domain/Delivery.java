package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {
    @Id @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy="delivery",fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING) // Enumerated의 기본값은 ORDINAL로 DB에 숫자로 들어갑니다. 반드시 STRING으로 변경해 문자로 저장해야 합니다.
    private DeliveryStatus status; // READY, COMP
}
