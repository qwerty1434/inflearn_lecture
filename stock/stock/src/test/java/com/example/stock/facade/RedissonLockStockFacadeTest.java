package com.example.stock.facade;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedissonLockStockFacadeTest {
    @Autowired
    private RedissonLockStockFacade redissonLockStockFacade;
    @Autowired
    private StockRepository stockRepository;

    @AfterEach
    public void tearDown(){
        stockRepository.deleteAllInBatch();
    }

    @DisplayName("동시에 100개의 요청이 들어오는 경우")
    @Test
    public void multipleRequestSimultaneously() throws InterruptedException {
        // given
        Stock stock = new Stock(1L, 100L);
        stockRepository.save(stock);
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount); // 다른 쓰레드의 작업이 끝날 때까지 대기할 수 있게 도와주는 클래스

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    redissonLockStockFacade.decrease(1L, 1L);
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