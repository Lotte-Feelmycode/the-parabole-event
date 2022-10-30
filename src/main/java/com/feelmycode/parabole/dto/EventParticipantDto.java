package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.EventParticipant;
import com.feelmycode.parabole.domain.EventPrize;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class EventParticipantDto {

    private Long id;
    private Long userId;
    private List<EventPrize> eventPrizes;
    private LocalDateTime eventTimeStartAt;

    public EventParticipantDto(EventParticipant eventParticipant) {
        id = eventParticipant.getId();
        userId = eventParticipant.getUserId();
        eventPrizes = eventParticipant.getEvent().getEventPrizes().stream().toList();
        eventTimeStartAt = eventParticipant.getEventTimeStartAt();
    }

}
