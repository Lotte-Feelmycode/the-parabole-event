package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.EventWinner;
import com.feelmycode.parabole.dto.EventWinnerDto;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.EventRepository;
import com.feelmycode.parabole.repository.EventWinnerRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventWinnerService {

    private final EventWinnerRepository eventWinnerRepository;

    public List<EventWinnerDto> eventWinnerList(Long eventId) {
        List<EventWinner> eventWinnerList = eventWinnerRepository.findAllByEventId(eventId);

        if (eventWinnerList == null) {
            throw new ParaboleException(HttpStatus.NOT_FOUND, "아직 이벤트가 종료되지 않았습니다");
        }
        return eventWinnerList.stream().map(EventWinnerDto::new).collect(Collectors.toList());
    }
}
