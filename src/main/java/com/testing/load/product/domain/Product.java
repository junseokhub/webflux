package com.testing.load.product.domain;

import com.testing.load.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@NoArgsConstructor
@Table("products")
public class Product extends BaseEntity {

    @Id
    private Long id;
    private String name;
    private int price;
    private int stock;
    private Long createdBy;
    @Version
    private int version;

    @Builder
    public Product(String name, int price, int stock, Long createdBy) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.createdBy = createdBy;
    }
}