package com.feelmycode.parabole.domain;

import com.feelmycode.parabole.dto.CouponDto;
import com.feelmycode.parabole.dto.ProductResponseDto;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Coupon {

    @Column(name = "coupon_id")
    private Long id;

    @Column(name = "coupon_name")
    private String name;

    @Column(name = "coupon_type")
    private String type;

    @Column(name = "coupon_detail")
    private String detail;

    @Column(name = "coupon_discount_value")
    private Integer discountValue;

    @Column(name = "coupon_expires_at")
    private LocalDateTime expiresAt;

    public Coupon(Long id, String name, String type, String detail, Integer discountValue,
        LocalDateTime expiresAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.detail = detail;
        this.discountValue = discountValue;
        this.expiresAt = expiresAt;
    }

    static public Coupon of(CouponDto couponDto) {
        return new Coupon(couponDto.getCouponId(), couponDto.getCouponName(),
            couponDto.getCouponType(), couponDto.getCouponDetail(),
            couponDto.getCouponDiscountValue(), couponDto.getExpiresAt());
    }

}
