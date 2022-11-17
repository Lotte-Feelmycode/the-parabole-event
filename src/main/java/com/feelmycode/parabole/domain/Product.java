package com.feelmycode.parabole.domain;

import com.feelmycode.parabole.dto.ProductResponseDto;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Product {

    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name")
    private String name;

    @Column(name = "product_img")
    private String thumbnailImg;

    public Product(Long id, String name, String thumbnailImg) {
        this.id = id;
        this.name = name;
        this.thumbnailImg = thumbnailImg;
    }

    static public Product of(ProductResponseDto productResponseDto) {
        return new Product(productResponseDto.getProductId(), productResponseDto.getProductName(),
            productResponseDto.getProductImg());
    }
}
