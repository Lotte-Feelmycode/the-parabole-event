package com.feelmycode.parabole.client;

import com.feelmycode.parabole.dto.CouponDto;
import com.feelmycode.parabole.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="parabole")
public interface ProductServiceClient {

    @GetMapping("/api/v1/product/data")
    ProductResponseDto getProduct(@RequestParam("productId") Long productId);

//    @GetMapping("/api/v1/product/{productId}/stock/{stock}")
//    Boolean addRemains(@RequestParam("productId") Long productId, @RequestParam("stock") Integer stock);

}
