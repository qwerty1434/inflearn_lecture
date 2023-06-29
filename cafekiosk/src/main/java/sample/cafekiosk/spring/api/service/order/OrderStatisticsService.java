package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.service.mail.MailService;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;

import java.time.LocalDate;
import java.util.List;

import static sample.cafekiosk.spring.domain.order.OrderStatus.PAYMENT_COMPLETED;

@RequiredArgsConstructor
@Service
public class OrderStatisticsService {

    private final OrderRepository orderRepository;
    private final MailService mailService;

    public boolean sendOrderStatisticsMail(LocalDate orderDate, String email){
        // 해당 일자에 결제완료된 주문을 가져와서
        List<Order> orders = orderRepository.findOrdersBy(
                orderDate.atStartOfDay(),
                orderDate.plusDays(1).atStartOfDay(),
                PAYMENT_COMPLETED
        );

        // 총 매출 합계를 계산하고
        int totalAmount = orders.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        // 메일을 전송 -> 외부 네트워크를 타는, 테스트에 불필요한 작업
        boolean result = mailService.sendMail("no-reply@cafekiosk.com",
                email,
                String.format("[매출 통계] %s",orderDate),
                String.format("총 매출 합계는 %s원입니다.",totalAmount)
        );

        if(!result){
            throw new IllegalArgumentException("매출 통계 메일 전송에 실패했습니다.");
         }

        return true;
    }

}
