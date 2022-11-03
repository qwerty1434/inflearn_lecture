package hello.core.discount;

import hello.core.member.Member;

public interface DiscountPolicy {

    // discount는 할인 대상 금액을 return해줌
    int discount(Member member, int price);
}
