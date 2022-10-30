package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventImage;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class EventListDto {

    private Long id;
    private SellerDto sellerId;
    private String createdBy;
    private String type;
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Integer status;
    private String descript;
    private EventImage eventImage;

    public EventListDto(Event event, SellerDto sellerId) {
        this.id = event.getId();
        this.sellerId = sellerId;
        this.createdBy = event.getCreatedBy();
        this.type = event.getType();
        this.title = event.getTitle();
        this.startAt = event.getStartAt();
        this.endAt = event.getEndAt();
        this.status = event.getStatus();
        this.descript = event.getDescript();
        this.eventImage = event.getEventImage();
    }
}
