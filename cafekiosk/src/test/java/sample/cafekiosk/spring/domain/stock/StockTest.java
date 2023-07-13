package sample.cafekiosk.spring.domain.stock;

import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    @DisplayName("재고의 수량이 제공된 수량보다 작은지 확인한다.")
    @Test
    void isQuantityLessThan(){
        // given
        Stock stock = Stock.create("001", 1);
        int quantity = 2;

        // when
        boolean result = stock.isQuantityLessThan(quantity);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("재고를 주어진 개수만큼 차감할 수 있다.")
    @Test
    void deductQuantity(){
        // given
        Stock stock = Stock.create("001", 1);
        int quantity = 1;

        // when
        stock.deductQuantity(quantity);

        // then
        assertThat(stock.getQuantity()).isZero();

    }

    @DisplayName("재고보다 많은 수의 수량으로 차감 시도하는 경우 예외가 발생한다.")
    @Test
    void deductQuantity2(){
        // given
        Stock stock = Stock.create("001", 1);
        int quantity = 2;

        // when // then
        assertThatThrownBy(() -> stock.deductQuantity(quantity))
                .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("차감할 재고 수량이 없습니다.");

    }

    @Disabled
    @DisplayName("")
    @TestFactory
    Collection<DynamicTest> dynamicTest(){
        return List.of(
                DynamicTest.dynamicTest("",() -> {

                }),

                DynamicTest.dynamicTest("",() -> {

                })
        );
    }

    @DisplayName("재고 차감 시나리오")
    @TestFactory
    Collection<DynamicTest> stockDeductionDynamicTest(){
        // given
        Stock stock = Stock.create("001",1);

        return List.of(
                DynamicTest.dynamicTest("재고를 주어진 개수만큼 차가할 수 있다.",() -> {
                    // given
                    int quantity = 1;

                    // when
                    stock.deductQuantity(quantity);

                    // then
                    assertThat(stock.getQuantity()).isZero();
                }),

                // 이전 시나리오에서 quantity가 1 차감된 상태가 여전히 유지되고 있음
                DynamicTest.dynamicTest("재고보다 많은 수의 수량으로 차감 시도하는 경우 예외가 발생한다",() -> {
                    // given
                    int quantity = 1;

                    // when // then
                    assertThatThrownBy(() -> stock.deductQuantity(quantity))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("차감할 재고 수량이 없습니다.");
                })
        );
    }

}