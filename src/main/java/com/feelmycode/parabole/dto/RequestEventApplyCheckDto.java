package com.feelmycode.parabole.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestEventApplyCheckDto {
    @NotNull
    private Long eventId;
    @NotNull
    private Long userId;

    public void setUserId(Long userId){
        this.userId = userId;
    }

    public RequestEventApplyCheckDto(Long eventId) {
        this.eventId = eventId;
    }

}
