package com.mvc.load.order;

import com.mvc.load.common.entity.BaseEntity;
import com.mvc.load.product.Product;
import com.mvc.load.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "original_price")
    private Integer originalPrice;

    @Column(name = "final_price")
    private Integer finalPrice;

    @Column(name = "status")
    private String status;

    @Builder
    public Order(User user, Product product, Integer originalPrice, Integer finalPrice, String status) {
        this.user = user;
        this.product = product;
        this.originalPrice = originalPrice;
        this.finalPrice = finalPrice;
        this.status = status;
    }

    public static Order pending(Long userId, Long productId) {
        Order order = new Order();
        order.status = "PENDING";
        return order;
    }



}
