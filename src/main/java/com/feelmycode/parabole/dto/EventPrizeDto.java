package com.feelmycode.parabole.dto;


import com.feelmycode.parabole.domain.EventPrize;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EventPrizeDto {


    private Long eventPrizeId;
    private String prizeType;
    private Integer stock;
    private Long productId;
    private String productName;
    private String productImg;
    private Long couponId;
    private String couponDetail;
    private Integer couponDiscountValue;
    private LocalDateTime expiresAt;

    public EventPrizeDto(Long eventPrizeId, String prizeType, Integer stock, Long productId, String productName, String productImg) {
        this.eventPrizeId = eventPrizeId;
        this.prizeType = prizeType;
        this.productId = productId;
        this.stock = stock;
        this.productName = productName;
        this.productImg = productImg;
    }

    public EventPrizeDto(Long eventPrizeId, String prizeType, Integer stock, Long couponId, String couponDetail, Integer couponDiscountValue, LocalDateTime expiresAt) {
        this.eventPrizeId = eventPrizeId;
        this.prizeType = prizeType;
        this.couponId = couponId;
        this.couponDetail = couponDetail;
        this.couponDiscountValue = couponDiscountValue;
        this.stock = stock;
        this.expiresAt = expiresAt;
    }

}
