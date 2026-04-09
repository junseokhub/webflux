package com.testing.load.common.config;

import com.testing.load.common.properties.AppProperties;
import com.testing.load.coupon.repository.CouponIssueRepository;
import com.testing.load.coupon.repository.CouponRepository;
import com.testing.load.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final CouponRepository couponRepository;
    private final CouponIssueRepository couponIssueRepository;
    private final UserRepository userRepository;
    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
    private final AppProperties appProperties;

    @Override
    public void run(ApplicationArguments args) {
        if (!appProperties.dataInit().enabled()) {
            log.info("데이터 초기화 스킵");
            return;
        }
        // 1. coupon_issues 초기화
        couponIssueRepository.deleteAll()
                .doOnSuccess(r -> log.info("coupon_issues 초기화 완료"))
                .doOnError(e -> log.error("coupon_issues 초기화 실패: {}", e.getMessage()))
                .subscribe();

        // 2. Redis refresh 토큰 초기화 (유저 ID 기반으로 하나씩 삭제)
        userRepository.findAll()
                .flatMap(user -> reactiveRedisTemplate.delete("refresh:" + user.getId())
                        .doOnSuccess(r -> log.info("유저 {} refresh 토큰 삭제 완료", user.getId())))
                .doOnError(e -> log.error("refresh 토큰 삭제 실패: {}", e.getMessage()))
                .subscribe();

        // 3. 쿠폰 Redis 재고 초기화
        couponRepository.findAll()
                .flatMap(coupon -> {
                    String stockKey = "{coupon:" + coupon.getId() + "}:stock";
                    String usersKey = "{coupon:" + coupon.getId() + "}:users";

                    return reactiveRedisTemplate.opsForValue()
                            .set(stockKey, String.valueOf(coupon.getTotalStock()))
                            .then(reactiveRedisTemplate.delete(usersKey))
                            .doOnSuccess(r -> log.info(
                                    "쿠폰 ID: {} 이름: {} 재고: {} Redis 초기화 완료",
                                    coupon.getId(), coupon.getName(), coupon.getTotalStock()
                            ));
                })
                .doOnError(e -> log.error("Redis 초기화 실패: {}", e.getMessage()))
                .subscribe();
    }
}