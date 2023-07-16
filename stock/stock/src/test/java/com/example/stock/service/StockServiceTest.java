package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StockServiceTest {
    @Autowired
    private PessimisticLockStockService stockService;

    @Autowired
    private StockRepository stockRepository;


    @AfterEach
    public void tearDown(){
        stockRepository.deleteAllInBatch();
    }

    @Test
    public void stockDecrease(){
        // given
        Stock stock = new Stock(1L, 100L);
        stockRepository.save(stock);

        // when
        stockService.decrease(1L, 1L);
        Stock decreasedStock = stockRepository.findById(1L).orElseThrow();

        // then
        assertThat(decreasedStock.getQuantity()).isEqualTo(99);
    }

    @DisplayName("동시에 100개의 요청이 들어오는 경우")
    @Test
    public void multipleRequestSimultaneously() throws InterruptedException{
        // given
        Stock stock = new Stock(1L, 100L);
        stockRepository.save(stock);
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount); // 다른 쓰레드의 작업이 끝날 때까지 대기할 수 있게 도와주는 클래스

        // when
        for(int i = 0; i < threadCount; i++){
            executorService.submit(() -> {
                try{
                    stockService.decrease(1L,1L);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Stock result = stockRepository.findById(1L).orElseThrow();

        // then
        assertThat(result.getQuantity()).isEqualTo(0L);
    }


}