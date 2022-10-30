package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Event;
import java.util.ArrayList;
import java.util.List;

public class SellerDto {

    private Long sellerId;

    private String storeName;

    private List<Event> events = new ArrayList<>();

    private List<CouponDetailDto> coupons = new ArrayList<>();

    public SellerDto(Long sellerId, String storeName, List<Event> events, List<CouponDetailDto> coupons){
        this.sellerId = sellerId;
        this.storeName = storeName;
        this.events = events;
        this.coupons = coupons;
    }
}
