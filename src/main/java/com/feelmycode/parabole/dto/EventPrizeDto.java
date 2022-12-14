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

    // Product
    private Long productId;
    private String productName;
    private String productImg;

    // Coupon
    private Long couponId;
    private String couponName;
    private String couponType;
    private String couponDetail;
    private Integer couponDiscountValue;
    private LocalDateTime expiresAt;

    public EventPrizeDto(Long eventPrizeId, String prizeType, Integer stock, Long productId,
        String productName, String productImg) {
        this.eventPrizeId = eventPrizeId;
        this.prizeType = prizeType;
        this.productId = productId;
        this.stock = stock;
        this.productName = productName;
        this.productImg = productImg;
    }

    public EventPrizeDto(Long eventPrizeId, String prizeType, Integer stock, Long couponId,
        String couponName, String couponType, String couponDetail, Integer couponDiscountValue,
        LocalDateTime expiresAt) {
        this.eventPrizeId = eventPrizeId;
        this.prizeType = prizeType;
        this.couponId = couponId;
        this.couponName = couponName;
        this.couponType = couponType;
        this.couponDetail = couponDetail;
        this.couponDiscountValue = couponDiscountValue;
        this.stock = stock;
        this.expiresAt = expiresAt;
    }

    static public EventPrizeDto toProductDto(EventPrize eventPrize) {
        return new EventPrizeDto(
            eventPrize.getId(), eventPrize.getPrizeType(), eventPrize.getStock(),
            eventPrize.getProduct().getId(), eventPrize.getProduct().getName(),
            eventPrize.getProduct().getThumbnailImg()
        );
    }

    static public EventPrizeDto toCouponDto(EventPrize eventPrize) {
        return new EventPrizeDto(
            eventPrize.getId(), eventPrize.getPrizeType(), eventPrize.getStock(),
            eventPrize.getCoupon().getId(), eventPrize.getCoupon().getName(),
            eventPrize.getCoupon().getType(), eventPrize.getCoupon().getDetail(),
            eventPrize.getCoupon().getDiscountValue(), eventPrize.getCoupon().getExpiresAt()
        );
    }

}
