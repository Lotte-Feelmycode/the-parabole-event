package com.feelmycode.parabole.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class EventPrizeCreateRequestDto {

    private Long id;
    private String type;
    private Integer stock;

    public EventPrizeCreateRequestDto(Long id, String type, Integer stock) {
        this.id = id;
        this.type = type;
        this.stock = stock;
    }
}
