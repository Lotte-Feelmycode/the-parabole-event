package com.feelmycode.parabole.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EventApplyTestDto {

    @NotNull
    private Long userId;
    @NotNull
    private Long eventId;
    @NotNull
    private Long eventPrizeId;

    public EventApplyTestDto(Long userId, Long eventId, Long eventPrizeId){
        this.userId = userId;
        this.eventId = eventId;
        this. eventPrizeId = eventPrizeId;
    }
}
