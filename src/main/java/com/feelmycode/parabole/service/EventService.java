package com.feelmycode.parabole.service;


import com.feelmycode.parabole.client.ProductServiceClient;
import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventPrize;
import com.feelmycode.parabole.dto.CouponDto;
import com.feelmycode.parabole.dto.EventCreateRequestDto;
import com.feelmycode.parabole.dto.EventListResponseDto;
import com.feelmycode.parabole.dto.EventPrizeCreateRequestDto;
import com.feelmycode.parabole.dto.EventPrizeDto;
import com.feelmycode.parabole.dto.EventSearchRequestDto;
import com.feelmycode.parabole.dto.EventSearchResponseDto;
import com.feelmycode.parabole.dto.ProductResponseDto;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.EventPrizeRepository;
import com.feelmycode.parabole.repository.EventRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ProductServiceClient ProductServiceClient;
    private final EventPrizeRepository eventPrizeRepository;

    private final EventParticipantService eventParticipantService;

    public List<EventListResponseDto> getEventListResponseDto(List<Event> eventEntities) {
        return eventEntities.stream()
            .map(EventListResponseDto::new)
            .collect(Collectors.toList());
    }

    /**
     * 이벤트 생성
     */
    // TODO: JWT 처리 후 userId 처리
    // TODO: @Valid
    @Transactional
    public Long createEvent(EventCreateRequestDto eventDto) {

        // 엔티티 조회
        Long sellerId = eventDto.getUserId();

        // 이벤트-경품정보 생성
        List<EventPrize> eventPrizeList = new ArrayList<>();

        List<EventPrizeCreateRequestDto> eventPrizeParams = eventDto.getEventPrizeCreateRequestDtos();

        if (!CollectionUtils.isEmpty(eventPrizeParams)) {
            for (EventPrizeCreateRequestDto eventPrizeParam : eventPrizeParams) {
                String prizeType = eventPrizeParam.getType();
                Long id = eventPrizeParam.getId();
                if (prizeType.equals("PRODUCT")) {
                    eventPrizeList.add(
                        new EventPrize(prizeType, eventPrizeParam.getStock(),
                            ProductServiceClient.getProduct(id).getProductId()));
                } else {
                    eventPrizeList.add(
                        new EventPrize(prizeType, eventPrizeParam.getStock(),
                            ProductServiceClient.getCouponData(id).getCouponId()));
                }
            }
        }

        // 이벤트 생성
        Event event = Event.builder()
            .sellerId(sellerId)
            .createdBy(eventDto.getCreatedBy()).type(eventDto.getType())
            .title(eventDto.getTitle()).startAt(eventDto.getStartAt()).endAt(eventDto.getEndAt())
            .descript(eventDto.getDescript()).eventImage(eventDto.getEventImage())
            .eventPrizes(eventPrizeList).build();

        // 이벤트 저장
        eventRepository.save(event);
        return event.getId();
    }

    /**
     * 이벤트 ID로 단건 조회
     */
    public EventListResponseDto getEventByEventId(Long eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND, "해당하는 ID의 이벤트가 없습니다"));
        List<EventPrize> eventPrize = eventPrizeRepository.findByEventId(eventId);
        List<EventPrizeDto> eventPrizeDtos = new ArrayList<>();
        for (EventPrize eventPrizes : eventPrize) {
            System.out.println(eventPrizes.getId() + " " + eventPrizes.getPrizeType());
            if (eventPrizes.getPrizeType().equals("PRODUCT")) {
                ProductResponseDto productResponseDto = ProductServiceClient.getProduct(
                    eventPrizes.getProductId());
                eventPrizeDtos.add(
                    new EventPrizeDto(eventPrizes.getId(), eventPrizes.getPrizeType(),
                        eventPrizes.getStock(),
                        productResponseDto.getProductId(), productResponseDto.getProductName(),
                        productResponseDto.getProductImg()));
            } else if (eventPrizes.getPrizeType().equals("COUPON")) {
                CouponDto couponDto = ProductServiceClient.getCouponData(eventPrizes.getCouponId());
                eventPrizeDtos.add(
                    new EventPrizeDto(eventPrizes.getId(), eventPrizes.getPrizeType(),
                        eventPrizes.getStock(), couponDto.getCouponId(),
                        couponDto.getCouponDetail(), couponDto.getCouponDiscountValue(),
                        couponDto.getExpiresAt()));
            }
        }
        return new EventListResponseDto(event, eventPrizeDtos);
    }

    /**
     * Seller ID로 이벤트 목록 조회
     */
    public List<Event> getEventsBySellerId(Long userId) {
        Long sellerId = userId;
        return eventRepository.findAllBySellerIdAndIsDeleted(sellerId, false);
    }

    /**
     * 검색 조건으로 이벤트 목록 조회 (경품 목록 제외)
     */
    public List<EventSearchResponseDto> getEventsSearch(
        EventSearchRequestDto eventSearchRequestDto) {

        List<Event> eventList = null;
        if (!StringUtils.isEmpty(eventSearchRequestDto.getEventType())) {
            eventList = eventRepository.findAllByTypeAndIsDeleted(
                eventSearchRequestDto.getEventType().getCode(), false);
        } else if (!StringUtils.isEmpty(eventSearchRequestDto.getEventTitle())) {
            eventList = eventRepository.findAllByTitleContainingAndIsDeleted(
                eventSearchRequestDto.getEventTitle(), false);
        } else if (!StringUtils.isEmpty(eventSearchRequestDto.getDateDiv())) {
            eventList = eventSearchRequestDto.getDateDiv() < 1
                ? eventRepository.findAllByStartAtBetweenAndIsDeleted(
                eventSearchRequestDto.getFromDateTime(), eventSearchRequestDto.getToDateTime(),
                false)
                : eventRepository.findAllByEndAtBetweenAndIsDeleted(
                    eventSearchRequestDto.getFromDateTime(), eventSearchRequestDto.getToDateTime(),
                    false);
        } else if (!StringUtils.isEmpty(eventSearchRequestDto.getEventStatus())) {
            eventList = eventRepository.findAllByStatusAndIsDeleted(
                eventSearchRequestDto.getEventStatus().getValue(), false);
        }

        return eventList.stream()
            .map(EventSearchResponseDto::new)
            .collect(Collectors.toList());
    }

    /**
     * 이벤트 전체 조회 (삭제된 이벤트 제외)
     */
    public List<Event> getEventsAllNotDeleted() {
        return eventRepository.findAllByIsDeleted(false);
    }

    // TODO : 이벤트 수정

    /**
     * 이벤트 취소
     */
    @Transactional
    public void cancelEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND, "해당 이벤트가 존재하지 않습니다."));
        try {
            event.cancel();
            eventRepository.save(event);
        } catch (Exception e) {
            throw new ParaboleException(HttpStatus.INTERNAL_SERVER_ERROR, "이벤트 등록 실패");
        }
    }

    public void updateEventStatus(LocalDateTime dateTime) {
        List<Event> eventBeforeList = eventRepository.findAllByStatus(0);
        List<Event> eventAfterList = eventRepository.findAllByStatus(1);

        List<Event> result = new ArrayList<>();
        if (eventBeforeList.size() != 0) {
            for (Event event : eventBeforeList) {
                if (event.getStartAt().isBefore(dateTime)) {
                    event.setStatus(1);
                }
                result.add(event);
            }
        }
        if (eventAfterList.size() != 0) {
            for (Event event : eventAfterList) {
                if (event.getEndAt().isBefore(dateTime) || event.getEndAt().equals(dateTime)) {
                    event.setStatus(2);
                    //랜덤 응모 이벤트 시작
                    log.info("event start {} =>");
                    eventParticipantService.eventRaffleStart(event.getId());
                }
                result.add(event);
            }
        }
        if (eventAfterList.size() != 0 || eventBeforeList.size() != 0) {
            eventRepository.saveAll(result);
        }
    }

    public void eventFCFSStart(LocalDateTime dateTime) {
        Event FCFSEvent = eventRepository.findByStatusAndType(1, "FCFS");
        if (FCFSEvent != null) {
            if (FCFSEvent.getEndAt().isBefore(dateTime) || FCFSEvent.getEndAt().isEqual(dateTime)) {
                FCFSEvent.setStatus(2);
                eventRepository.save(FCFSEvent);
                eventParticipantService.eventFCFSWinner(FCFSEvent.getId());
            }
        }

    }
}
