package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventParticipant;
import com.feelmycode.parabole.domain.EventWinner;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EventWinnerDto {

    private Long eventWinnerId;

    private String eventTitle;

    private String prizeName;

    private String userEmail;

    public EventWinnerDto(EventWinner eventWinner){
        this.eventWinnerId = eventWinner.getId();
        this.eventTitle = eventWinner.getEvent().getTitle();
        this.prizeName = eventWinner.getEventParticipant().getEventPrize().getPrizeName();
        this.userEmail = eventWinner.getEventParticipant().getUserEmail();
    }
}
