package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventParticipant;
import com.feelmycode.parabole.domain.EventPrize;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EventApplyDto {

    @NotNull
    private Long userId;
    @NotNull
    private Long eventId;
    @NotNull
    private Long eventPrizeId;
    private LocalDateTime participantAt;

    public EventApplyDto(Long userId, Long eventId, Long eventPrizeId,
        LocalDateTime participantAt) {
        this.userId = userId;
        this.eventId = eventId;
        this.eventPrizeId = eventPrizeId;
        this.participantAt = participantAt;
    }

    public EventApplyDto(Long userId, Long eventId, Long eventPrizeId) {
        this.userId = userId;
        this.eventId = eventId;
        this.eventPrizeId = eventPrizeId;
    }

    public EventParticipant toEntity(Long userId, Event event, EventPrize eventPrize,
        LocalDateTime eventTimeStartAt) {
        return new EventParticipant(userId, event, eventPrize, eventTimeStartAt);
    }
}
