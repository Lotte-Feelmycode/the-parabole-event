package com.feelmycode.parabole.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.Column;
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

    private Long couponId;

    private Long productId;

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Builder
    public EventPrize(String prizeType, Integer stock, Long prizeId) {
        this.prizeType = prizeType;
        this.stock = stock;
        if (prizeType.equals("PRODUCT")) {
            this.productId = prizeId;
        } else if (prizeType.equals("COUPON")) {
            this.couponId = prizeId;
        }
    }

}

