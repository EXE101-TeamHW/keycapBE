package com.keycap.keycapdesign.entity;

import com.keycap.keycapdesign.enums.KeyProfile;
import com.keycap.keycapdesign.enums.LayoutType;
import com.keycap.keycapdesign.enums.ProductStatus;
import com.keycap.keycapdesign.enums.ProductTheme;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductTheme theme;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LayoutType layoutType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KeyProfile keyProfile;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;

    @Column(columnDefinition = "text")
    private String imagesJson;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status = ProductStatus.ACTIVE;
}
