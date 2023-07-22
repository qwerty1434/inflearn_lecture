package com.example.api.service;

import com.example.api.domain.Coupon;
import com.example.api.producer.CouponCreateProducer;
import com.example.api.repository.AppliedUserRepository;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {
    private static final String COUPON_COUNT_KEY_NAME = "coupon_count";
    private static final String APPLIED_USER_KEY_NAME = "applied_user";
    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final CouponCreateProducer couponCreateProducer;
    private final AppliedUserRepository appliedUserRepository;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository, CouponCreateProducer couponCreateProducer, AppliedUserRepository appliedUserRepository) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
        this.couponCreateProducer = couponCreateProducer;
        this.appliedUserRepository = appliedUserRepository;
    }

    public void apply(Long userId){
        Long apply = appliedUserRepository.add(APPLIED_USER_KEY_NAME,userId);
        if(apply != 1) {
            throw new RuntimeException("이미 쿠폰을 발급받은 유저입니다.");
        }

        long count = couponCountRepository.increment(COUPON_COUNT_KEY_NAME);

        if(count > 100) return;
        couponCreateProducer.create(userId);
    }
}
