package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventParticipant;
import com.feelmycode.parabole.domain.EventWinner;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EventParticipantUserDto {

    private Long userId;
    private Long eventId;

    private LocalDateTime eventTimeStartAt;
    private String eventTitle;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Integer status;
    private String eventImg;
    private String winnerStatus;
    private String prizeName;

    private String eventType;

    public void setWinnerStatus(String winnerStatus) {
        this.winnerStatus = winnerStatus;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public EventParticipantUserDto(EventParticipant eventParticipant) {
        Event event = eventParticipant.getEvent();
        this.eventType = eventParticipant.getEvent().getType();
        this.userId = eventParticipant.getUserId();
        this.eventId = event.getId();
        this.eventTimeStartAt = eventParticipant.getEventTimeStartAt();
        this.eventTitle = event.getTitle();
        this.startAt = event.getStartAt();
        this.endAt = event.getEndAt();
        this.status = event.getStatus();
        this.eventImg = event.getEventImage().getEventBannerImg();
        this.winnerStatus = "미추첨";
    }
}
