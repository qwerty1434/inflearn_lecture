package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;


    @Test
    public void 상품주문() throws Exception{
        Member member = createMember();

        Book book = createBook("책이름",10000,10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        Order getOrder = orderRepository.findOne(orderId);

        Assert.assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER,getOrder.getStatus());
        Assert.assertEquals("주문한 상품 종류 수가 정확해야 한다", 1,getOrder.getOrderItems().size());
        Assert.assertEquals("주문한 상품 종류 수가 정확해야 한다", 10000 * orderCount,getOrder.getTotalPrice());
        Assert.assertEquals("주문 수량만큼 재고가 줄어야 한다", 8,book.getStockQuantity());

    }

    // @Test(expected = NotEnoughStockException.class) junit5버전에서는 안됨
    @Test
    public void 상품주문_재고수량초과() throws Exception{
        Member member = createMember();
        Item item = createBook("책이름",10000,10);
        int orderCount = 11;
        orderService.order(member.getId(), item.getId(),orderCount); // 여기서 NotEnoughStockException 예외가 발생해야 합니다.

        fail("재고 수량 부족 예외가 발생해야 한다."); // 위에서 예외가 안뜨고 여기까지 내려왔기 때문에 테스트를 실패(fail)로 처리해야 한다
    }

    @Test
    public void 주문취소() throws Exception{
        Member member = createMember();
        Book item = createBook("책", 10000, 10);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        orderService.cancelOrder(orderId);
        Order getOrder = orderRepository.findOne(orderId);
        Assert.assertEquals("주문 취소시 상태는 CANCEL이다.",OrderStatus.CANCEL, getOrder.getStatus());
        Assert.assertEquals("주문이 취소된 상품은 그만큼 재고가 복구돼 있어야 한다",10, item.getStockQuantity());
    }





    private Book createBook(String bookName, int price, int stockQuantity){
        Book book = new Book();
        book.setName(bookName);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember(){
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강가","123-123"));
        em.persist(member);
        return member;
    }

}