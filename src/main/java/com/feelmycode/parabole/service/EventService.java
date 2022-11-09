package com.feelmycode.parabole.service;


import com.feelmycode.parabole.client.ProductServiceClient;
import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventPrize;
import com.feelmycode.parabole.dto.CouponDto;
import com.feelmycode.parabole.dto.EventCreateRequestDto;
import com.feelmycode.parabole.dto.EventListResponseDto;
import com.feelmycode.parabole.dto.EventPrizeCreateRequestDto;
import com.feelmycode.parabole.dto.EventPrizeDto;
import com.feelmycode.parabole.dto.EventSearchResponseDto;
import com.feelmycode.parabole.dto.ProductResponseDto;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.EventPrizeRepository;
import com.feelmycode.parabole.repository.EventRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ProductServiceClient productServiceClient;
    private final EventPrizeRepository eventPrizeRepository;

    private final EventParticipantService eventParticipantService;

    public List<EventListResponseDto> getEventListResponseDto(List<Event> eventEntities) {
        return eventEntities.stream()
            .map(EventListResponseDto::new)
            .collect(Collectors.toList());
    }


    private EventPrizeDto productPrize(EventPrize eventPrize) {
        ProductResponseDto product = productServiceClient.getProduct(eventPrize.getProductId());

        return new EventPrizeDto(eventPrize.getId(), eventPrize.getPrizeType(),
            eventPrize.getStock(),
            product.getProductId(), product.getProductName(), product.getProductImg());
    }

    private EventPrizeDto couponPrize(EventPrize eventPrize) {
        CouponDto coupon = productServiceClient.getCouponData(eventPrize.getCouponId());

        return new EventPrizeDto(eventPrize.getId(), eventPrize.getPrizeType(),
            eventPrize.getStock(), coupon.getCouponId(), coupon.getCouponDetail(),
            coupon.getCouponDiscountValue(), coupon.getExpiresAt());
    }


    /**
     * 이벤트 생성
     */
    // TODO: JWT 처리 후 userId 처리
    // TODO: @Valid
    @Transactional
    public Long createEvent(EventCreateRequestDto eventDto) {
        log.info("이벤트 서비스 In");

        // 엔티티 조회
        // seller id 보내는걸로 변경
        Long sellerId = eventDto.getUserId();

        // 이벤트-경품정보 생성
        List<EventPrize> eventPrizeList = new ArrayList<>();

        List<EventPrizeCreateRequestDto> eventPrizeParams = eventDto.getEventPrizeCreateRequestDtos();

        if (!CollectionUtils.isEmpty(eventPrizeParams)) {
            log.info("eventPrizeParams 존재");

            for (EventPrizeCreateRequestDto eventPrizeParam : eventPrizeParams) {
                String prizeType = eventPrizeParam.getType();
                Long id = eventPrizeParam.getId();

                if (prizeType.equals("PRODUCT")) {
                    log.info("eventPrizeParams PRODUCT");
                    log.info("PRODUCT ID / eventPrizeParam.getId() : {}", id);
                    log.info("ParaboleServiceClient.getProduct(id) : {} ",
                        productServiceClient.getProduct(id).getProductId());
                    // 페인클라이언트로 조회가 안되는 상태임
                    eventPrizeList.add(
                        new EventPrize(prizeType, eventPrizeParam.getStock(),
                            productServiceClient.getProduct(id).getProductId()));
                } else {
                    log.info("eventPrizeParams COUPON");

                    eventPrizeList.add(
                        new EventPrize(prizeType, eventPrizeParam.getStock(),
                            productServiceClient.getCouponData(id).getCouponId()));
                }
            }
        }

        log.info("eventPrizeList 생성 완료"  + eventPrizeList.size());

        // 이벤트 생성
        Event event = Event.builder()
            .sellerId(sellerId)
            .createdBy(eventDto.getCreatedBy()).type(eventDto.getType())
            .title(eventDto.getTitle()).startAt(eventDto.getStartAt()).endAt(eventDto.getEndAt())
            .descript(eventDto.getDescript()).eventImage(eventDto.getEventImage())
            .eventPrizes(eventPrizeList).build();

        log.info("event객체 생성 완료"  + event.getTitle());

        // 이벤트 저장
        eventRepository.save(event);

        log.info("event객체 저장 완료"  + event.getId());

        return event.getId();
    }

    /**
     * 이벤트 ID로 단건 조회
     */
    public EventListResponseDto getEventByEventId(Long eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND, "해당하는 ID의 이벤트가 없습니다"));

        List<EventPrize> eventPrizes = eventPrizeRepository.findByEventId(eventId);
        List<EventPrizeDto> eventPrizeDtos = new ArrayList<>();

        eventPrizes.forEach(e -> {
            if (e.getPrizeType().equals("PRODUCT")) {
                eventPrizeDtos.add(productPrize(e));
            } else if (e.getPrizeType().equals("COUPON")) {
                eventPrizeDtos.add(couponPrize(e));
            }
        });

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
        String eventType, String eventTitle, Integer dateDiv, LocalDateTime fromDateTime,
        LocalDateTime toDateTime, Integer eventStatus) {

        List<Event> eventList = null;
        List<String> types =
            eventType.equals("") ? Arrays.asList("RAFFLE", "FCFS") : Arrays.asList(eventType);
        List<Integer> statuses =
            eventStatus < 0 ? Arrays.asList(0, 1, 2) : Arrays.asList(eventStatus);

        if (dateDiv > -1) {
            eventList = dateDiv < 1
                ? eventRepository.findAllByStartAtBetweenAndIsDeleted(fromDateTime, toDateTime,
                false)
                : eventRepository.findAllByEndAtBetweenAndIsDeleted(fromDateTime, toDateTime,
                    false);
        } else if (eventTitle.equals("")) {
            eventList = eventRepository.findAllByTypeInAndStatusInAndIsDeleted(
                types, statuses, false);
        } else {
            eventList = eventRepository.findAllByTypeInAndStatusInAndTitleContainingAndIsDeleted(
                types, statuses, eventTitle, false);
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

    /**
     * 이벤트 조회 (셀러 오피스 이벤트 목록 조회용)
     */
    public List<EventSearchResponseDto> getEventsMonthAfter() {
        LocalDateTime nowDate = LocalDateTime.now();
        LocalDateTime monthAfterDate = nowDate.plusMonths(1L);
        return eventRepository.findAllByStartAtBetweenAndIsDeleted(nowDate,
                monthAfterDate, false)
            .stream()
            .filter(event -> event.getType().equals("FCFS"))
            .sorted(Comparator.comparing(Event::getStartAt))
            .map(EventSearchResponseDto::new)
            .collect(Collectors.toList());
    }

    /**
     * 이벤트 취소
     */
    @Transactional
    public void cancelEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND, "해당 이벤트가 존재하지 않습니다."));
        try {
            event.cancel();
//            event.getEventPrizes().forEach(e -> {
//                if (e.getPrizeType().equals("PRODUCT")) {
//                    ProductServiceClient.addRemains(e.getProductId(), e.getStock());
//                } else if (e.getPrizeType().equals("COUPON")) {
//                    couponServiceClient.cancelCouponEvent(e.getCouponId(), e.getStock());
//                }
//            });
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
