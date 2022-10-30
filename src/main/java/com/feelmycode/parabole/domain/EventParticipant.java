package com.feelmycode.parabole.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "event_participants")
@NoArgsConstructor
public class EventParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_participant_id")
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name="event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name="event_prize_id")
    private EventPrize eventPrize;

    @Column(columnDefinition = "DateTime(6) not null")
    private LocalDateTime eventTimeStartAt;

    public EventParticipant(Long userId, Event event, EventPrize eventPrize,
        LocalDateTime eventTimeStartAt) {
        this.userId = userId;
        this.event = event;
        this.eventPrize = eventPrize;
        this.eventTimeStartAt = eventTimeStartAt;
    }
}
