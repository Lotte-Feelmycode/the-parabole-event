package com.feelmycode.parabole.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_prizes")
@Getter
@NoArgsConstructor
public class EventPrize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_prize_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonBackReference
    private Event event;

    private String prizeType; // [COUPON, PRODUCT]

    private Integer stock;

    @Embedded
    private Product product;

    @Embedded
    private Coupon coupon;

//    private Long couponId;
//
//    private Long productId;
//
//    private String prizeName;

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Builder
    public EventPrize(String prizeType, Integer stock, Product product) {
        this.prizeType = prizeType;
        this.stock = stock;
        this.product = product;
    }

    @Builder
    public EventPrize(String prizeType, Integer stock, Coupon coupon) {
        this.prizeType = prizeType;
        this.stock = stock;
        this.coupon = coupon;
    }
}

