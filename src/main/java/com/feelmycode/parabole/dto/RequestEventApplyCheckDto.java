package com.feelmycode.parabole.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestEventApplyCheckDto {
    @NotNull
    private Long eventId;

    public RequestEventApplyCheckDto(Long eventId) {
        this.eventId = eventId;
    }

}
