package com.feelmycode.parabole.client;

import com.feelmycode.parabole.dto.CouponDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "parabole")
public interface CouponServiceClient {

    @GetMapping("/api/v1/coupon/data")
    CouponDto getCouponData(@RequestParam("couponId") Long couponId);

    @GetMapping("/api/v1/coupon/{couponId}/stock/{stock}")
    Boolean cancelCouponEvent(@RequestParam("couponId") Long couponId,
        @RequestParam("stock") Integer stock);
}
