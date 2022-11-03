package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventParticipant;
import com.feelmycode.parabole.domain.EventWinner;
import com.feelmycode.parabole.dto.EventApplyDto;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.EventParticipantRepository;
import com.feelmycode.parabole.domain.EventPrize;
import com.feelmycode.parabole.dto.EventParticipantDto;
import com.feelmycode.parabole.dto.EventParticipantUserDto;
import com.feelmycode.parabole.dto.RequestEventApplyCheckDto;
import com.feelmycode.parabole.repository.EventPrizeRepository;
import com.feelmycode.parabole.repository.EventRepository;
import com.feelmycode.parabole.repository.EventWinnerRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class EventParticipantService {

    private final EventParticipantRepository eventParticipantRepository;
    private final EventRepository eventRepository;
    private final EventPrizeRepository eventPrizeRepository;

    private final EventWinnerRepository eventWinnerRepository;

    public List<EventParticipantUserDto> getEventParticipantUser(Long userId) {
        List<EventParticipant> eventParticipant = eventParticipantRepository.findAllByUserId(
            userId);

        if (eventParticipant == null) {
            throw new ParaboleException(HttpStatus.NOT_FOUND, "이벤트 참여내역이 없습니다.");
        }
        return eventParticipant.stream().map(EventParticipantUserDto::new)
            .collect(Collectors.toList());
    }

    public void eventJoin(EventApplyDto eventApplyDto) {
        applyCheck(eventApplyDto);
        EventParticipant eventApply = eventApplyDto.toEntity(
            eventApplyDto.getUserId(),
            getEvent(eventApplyDto.getEventId()),
            getEventPrize(eventApplyDto.getEventPrizeId()),
            LocalDateTime.now());

        eventParticipantRepository.save(eventApply);
    }

    public boolean eventApplyCheck(RequestEventApplyCheckDto dto) {
        EventParticipant eventParticipant = eventParticipantRepository.findByUserIdAndEventId(
            dto.getUserId(), dto.getEventId());
        if (eventParticipant != null) {
            return false;
        }
        return true;
    }

    public List<EventParticipantDto> getEventParticipants(Long eventId) {
        List<EventParticipant> eventParticipantList = eventParticipantRepository.findAllByEventId(
            eventId);

        return eventParticipantList.stream()
            .map(EventParticipantDto::new)
            .collect(Collectors.toList());
    }

    public void eventRaffleStart(Long eventId) {
        List<EventParticipant> eventParticipantList = eventParticipantRepository.findAllByEventId(
                eventId).stream().sorted(
                Comparator.comparing(eventParticipant -> eventParticipant.getEventPrize().getId()))
            .toList();

        Long prizeId = null;
        List<EventParticipant> raffleList = new ArrayList<>();
        for (EventParticipant eventParticipant : eventParticipantList) {
            if (prizeId != eventParticipant.getEventPrize().getId()) {
                prizeId = eventParticipant.getEventPrize().getId();
                if (raffleList.size() != 0) {
                    createRandomApply(raffleList);
                }
                raffleList = new ArrayList<>();
            }
            raffleList.add(eventParticipant);
        }
        createRandomApply(raffleList);
    }

    public void eventFCFSWinner(Long eventId) {
        List<EventParticipant> eventParticipantList =
            eventParticipantRepository.findAllByEventId(
                    eventId).stream().sorted(
                    Comparator.comparing(eventParticipant -> eventParticipant.getEventPrize().getId()))
                .sorted(Comparator.comparing(
                    eventParticipant -> eventParticipant.getEventTimeStartAt())).toList();

        Long prizeId = null;
        Integer stock = 0;
        for (EventParticipant eventParticipant : eventParticipantList) {
            if (prizeId != eventParticipant.getEventPrize().getId()) {
                prizeId = eventParticipant.getEventPrize().getId();
                stock = eventParticipant.getEventPrize().getStock();
            }
            if (stock != 0) {
                stock--;
                eventWinnerRepository.save(
                    new EventWinner(eventParticipant.getUserId(), eventParticipant.getEvent(),
                        eventParticipant.getEventPrize()));
            }
            EventPrize resultEventPrize = getEventPrize(prizeId);
            resultEventPrize.setStock(stock);
            eventPrizeRepository.save(resultEventPrize);

        }
    }

    public void applyCheck(EventApplyDto eventApplyDto) {
        EventParticipant eventParticipant = eventParticipantRepository.findByUserIdAndEventId(
            eventApplyDto.getUserId(), eventApplyDto.getEventId());
        if (eventParticipant != null) {
            throw new ParaboleException(HttpStatus.ALREADY_REPORTED, "이미 응모 완료 되었습니다");
        }
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND, "존재하지 않는 이벤트 입니다"));
    }

    private EventPrize getEventPrize(Long eventPrizeId) {
        return eventPrizeRepository.findById(eventPrizeId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다"));
    }

    private void createRandomApply(List<EventParticipant> raffleList) {
        Long prizeId = raffleList.get(0).getEventPrize().getId();
        Event raffleEvent = raffleList.get(0).getEvent();
        EventPrize eventPrize = getEventPrize(prizeId);
        Integer stock = eventPrize.getStock();
        Random randomNum = new Random();
        Map<Long, Integer> applyMap = new HashMap<>();
        for (EventParticipant raffle : raffleList) {
            randomNum.setSeed(
                raffle.getEventTimeStartAt().atZone(ZoneId.of("Asia/Seoul")).toInstant()
                    .toEpochMilli());
            Integer num = (randomNum.nextInt(raffleList.size()) + 1);
            applyMap.put(raffle.getUserId(), num);
        }
        List<Map.Entry<Long, Integer>> entryList = new LinkedList<>(applyMap.entrySet());
        entryList.sort(Map.Entry.comparingByValue());
        for (Map.Entry<Long, Integer> entry : entryList) {
            if (stock != 0) {
                eventWinnerRepository.save(
                    new EventWinner(entry.getKey(), raffleEvent, eventPrize));
                stock--;
            }
        }
        eventPrize.setStock(stock);
        eventPrizeRepository.save(eventPrize);
    }

}
