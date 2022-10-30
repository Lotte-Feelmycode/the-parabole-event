package com.feelmycode.parabole.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CouponDetailDto {

    private Long id;

    private String name;

    private String type;

    private Integer discountValue;

    private LocalDateTime validAt;

    private LocalDateTime expiresAt;

    private Long maxDiscountAmount;

    private Long minPaymentAmount;

    private String detail;

    private Integer cnt;

    public CouponDetailDto(Long id, String name, String type, Integer discountValue, LocalDateTime validAt, LocalDateTime expiresAt, Long maxDiscountAmount, Long minPaymentAmount, String detail, Integer cnt){
        this.id = id;
        this.name = name;
        this.type = type;
        this.discountValue = discountValue;
        this.validAt = validAt;
        this.expiresAt = expiresAt;
        this.maxDiscountAmount = maxDiscountAmount;
        this.minPaymentAmount = minPaymentAmount;
        this.detail = detail;
        this.cnt = cnt;
    }
}
