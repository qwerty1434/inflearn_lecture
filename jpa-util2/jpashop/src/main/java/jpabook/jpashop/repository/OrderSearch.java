package jpabook.jpashop.repository;

import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch { // 검색 조건 파라미터를 담고 있는 클래스(Dto같은 역할)
    private String memberName; // 회원 이름
    private OrderStatus orderStatus; // 주문 상태
}
