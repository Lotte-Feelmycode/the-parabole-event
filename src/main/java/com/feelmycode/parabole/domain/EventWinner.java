package com.feelmycode.parabole.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "event_winners")
@Getter
@NoArgsConstructor
@ToString
public class EventWinner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name="event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name="event_prize_id")
    private EventPrize eventPrize;

    @ManyToOne
    @JoinColumn(name="event_participant_id")
    private EventParticipant eventParticipant;

    public EventWinner(Long userId, Event event, EventPrize eventPrize){
        this.userId = userId;
        this.event = event;
        this.eventPrize = eventPrize;
    }
}
