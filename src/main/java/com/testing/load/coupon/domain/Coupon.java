package com.testing.load.coupon.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Table("coupons")
public class Coupon {

    @Id
    private Long id;
    private String name;
    private int totalStock;
    private int remainingStock;
    @CreatedDate
    private LocalDateTime createdAt;

    public Coupon(String name, int totalStock) {
        this.name = name;
        this.totalStock = totalStock;
        this.remainingStock = totalStock;
    }
}