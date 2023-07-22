package com.example.api.service;

import com.example.api.repository.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplyServiceTest {
    @Autowired
    private ApplyService applyService;
    @Autowired
    private CouponRepository couponRepository;

    @DisplayName("한번만 응모")
    @Test
    public void applyOnce(){
        applyService.apply(1L);
        long count = couponRepository.count();
        assertThat(count).isEqualTo(1);
    }

    @DisplayName("멀티쓰레드 환경에서 1000개의 요청이 동시에 들어옴")
    @Test
    public void concurrentApply() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try{
                    applyService.apply(userId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Thread.sleep(10000);

        long count = couponRepository.count();
        assertThat(count).isEqualTo(100);
    }

    @DisplayName("중복된 쿠폰 발급 요청이 들어옴")
    @Test
    public void duplicateApply() throws InterruptedException {
        int threadCount = 1000;

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            long userId = 1; // 모든 쓰레드에서 userId는 1
            executorService.submit(() -> {
                try{
                    applyService.apply(userId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Thread.sleep(10000);

        long count = couponRepository.count();
        assertThat(count).isEqualTo(1);
    }


}