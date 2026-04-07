package com.testing.load.coupon.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Table("coupon_issues")
public class CouponIssue {

    @Id
    private Long id;
    private Long couponId;
    private Long userId;
    private LocalDateTime issuedAt;

    public CouponIssue(Long couponId, Long userId) {
        this.couponId = couponId;
        this.userId = userId;
    }
}