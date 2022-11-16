package com.feelmycode.parabole.client;

import com.feelmycode.parabole.dto.CouponDto;
import com.feelmycode.parabole.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "parabole")
public interface ParaboleServiceClient {

    @GetMapping("/api/v1/health")
    Boolean healthCheck();

    @GetMapping("/api/v1/product/data")
    ProductResponseDto getProduct(@RequestParam("productId") Long productId);

    @GetMapping("/api/v1/coupon/data")
    CouponDto getCoupon(@RequestParam("couponId") Long couponId);

    @GetMapping("/api/v1/product/{productId}/stock/{stock}")
    Boolean setProductRemains(@PathVariable("productId") Long productId,
        @PathVariable("stock") Integer stock);

    @GetMapping("/api/v1/coupon/{couponId}/stock/{stock}")
    Boolean setCouponRemains(@PathVariable("couponId") Long couponId,
        @PathVariable("stock") Integer stock);


}
