package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventWinner;
import com.feelmycode.parabole.dto.EventWinnerDto;
import com.feelmycode.parabole.repository.EventRepository;
import com.feelmycode.parabole.repository.EventWinnerRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventWinnerService {

    private final EventWinnerRepository eventWinnerRepository;
    private final EventRepository eventRepository;

    public List<EventWinnerDto> eventWinnerList(Long sellerId) {
        Integer eventEndStatus = 2;
        List<EventWinner> eventWinnerList = new ArrayList<>();
        List<Event> eventList = eventRepository.findAllBySellerIdAndStatus(sellerId, eventEndStatus);

        for (Event event : eventList) {
            eventWinnerList = eventWinnerRepository.findAllByEventId(event.getId());
        }
        return eventWinnerList.stream().map(EventWinnerDto::new).collect(Collectors.toList());
    }
}
