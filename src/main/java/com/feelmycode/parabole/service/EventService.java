package com.feelmycode.parabole.service;


import com.feelmycode.parabole.client.ParaboleServiceClient;
import com.feelmycode.parabole.domain.Coupon;
import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventPrize;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.dto.CouponDto;
import com.feelmycode.parabole.dto.EventCreateRequestDto;
import com.feelmycode.parabole.dto.EventListResponseDto;
import com.feelmycode.parabole.dto.EventPrizeCreateRequestDto;
import com.feelmycode.parabole.dto.EventPrizeDto;
import com.feelmycode.parabole.dto.EventSearchResponseDto;
import com.feelmycode.parabole.dto.ProductResponseDto;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.global.util.StringUtil;
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
    private final ParaboleServiceClient paraboleServiceClient;
    private final EventPrizeRepository eventPrizeRepository;
    private final EventParticipantService eventParticipantService;

    public List<EventListResponseDto> getEventListResponseDto(List<Event> eventEntities) {
        return eventEntities.stream()
            .map(EventListResponseDto::new)
            .collect(Collectors.toList());
    }

    private EventPrizeDto productPrize(EventPrize eventPrize) {
        ProductResponseDto product = paraboleServiceClient.getProduct(
            eventPrize.getProduct().getId());

        return new EventPrizeDto(eventPrize.getId(), eventPrize.getPrizeType(),
            eventPrize.getStock(), product.getProductId(), product.getProductName(),
            product.getProductImg());
    }

    private EventPrizeDto couponPrize(EventPrize eventPrize) {
        CouponDto coupon = paraboleServiceClient.getCoupon(eventPrize.getCoupon().getId());

        return new EventPrizeDto(eventPrize.getId(), eventPrize.getPrizeType(),
            eventPrize.getStock(), coupon.getCouponId(), coupon.getCouponName(),
            coupon.getCouponType(), coupon.getCouponDetail(), coupon.getCouponDiscountValue(),
            coupon.getExpiresAt());
    }

    /**
     * 이벤트 생성
     */
    @Transactional
    public Long createEvent(Long sellerId, EventCreateRequestDto eventDto) {

        log.info("이벤트 생성 {}", eventDto.toString());

        List<EventPrize> eventPrizeList = new ArrayList<>();

        List<EventPrizeCreateRequestDto> eventPrizeDtos = eventDto.getEventPrizeCreateRequestDtos();

        if (!CollectionUtils.isEmpty(eventPrizeDtos)) {
            for (EventPrizeCreateRequestDto eventPrize : eventPrizeDtos) {
                log.info("이벤트 경품 {}", eventPrize.toString());
                String prizeType = eventPrize.getType();
                Long id = eventPrize.getId();

                if (prizeType.equals("PRODUCT")) {
                    Product product = Product.of(paraboleServiceClient.getProduct(id));
                    log.info("product from market be {} :", product.getName());

                    eventPrizeList.add(
                        new EventPrize(prizeType, eventPrize.getStock(), product));

                    paraboleServiceClient.setProductRemains(product.getId(),
                        -1 * (eventPrize.getStock()));
                } else {
                    Coupon coupon = Coupon.of(paraboleServiceClient.getCoupon(id));
                    log.info("coupon from market be {} :", coupon.getName());

                    eventPrizeList.add(
                        new EventPrize(prizeType, eventPrize.getStock(), coupon));
                    paraboleServiceClient.setCouponRemains(coupon.getId(),
                        -1 * (eventPrize.getStock()));
                }
            }
        }

        LocalDateTime getStartAt = StringUtil.controllerParamIsBlank(eventDto.getStartAt()) ? null : LocalDateTime.parse(eventDto.getStartAt());
        LocalDateTime getEndAt = StringUtil.controllerParamIsBlank(eventDto.getEndAt()) ? null : LocalDateTime.parse(eventDto.getEndAt());
        // 이벤트 생성
        Event event = Event.builder()
            .sellerId(sellerId)
            .createdBy(eventDto.getCreatedBy()).type(eventDto.getType())
            .title(eventDto.getTitle()).startAt(getStartAt).endAt(getEndAt)
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

        List<EventPrize> eventPrizes = eventPrizeRepository.findByEventId(eventId);
        List<EventPrizeDto> eventPrizeDtos = new ArrayList<>();

        eventPrizes.forEach(e -> {
            if (e.getPrizeType().equals("PRODUCT")) {
                eventPrizeDtos.add(EventPrizeDto.toProductDto(e));
            } else if (e.getPrizeType().equals("COUPON")) {
                eventPrizeDtos.add(EventPrizeDto.toCouponDto(e));
            }
        });

        return new EventListResponseDto(event, eventPrizeDtos);
    }


    /**
     * Seller ID로 이벤트 목록 조회
     */
    public List<Event> getEventsBySellerId(Long sellerId) {
        return eventRepository.findAllBySellerIdAndIsDeleted(sellerId, false);
    }

    /**
     * 검색 조건으로 이벤트 목록 조회 (경품 목록 제외)
     */
    public List<EventSearchResponseDto> getEventsSearch(
        String eventType, String eventTitle, Integer dateDiv, LocalDateTime fromDateTime,
        LocalDateTime toDateTime, Integer eventStatus, Long sellerId) {

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

        if (sellerId != null) {
            return eventList.stream()
                .filter(event -> event.getSellerId().equals(sellerId))
                .map(EventSearchResponseDto::new)
                .collect(Collectors.toList());
        } else {
            return eventList.stream()
                .map(EventSearchResponseDto::new)
                .collect(Collectors.toList());
        }

    }

    /**
     * 이벤트 전체 조회 (삭제된 이벤트 제외)
     */
    public List<Event> getEventsAllNotDeleted() {
        return eventRepository.findAllByIsDeletedOrderByStartAtDescAndCreatedDesc(false);
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
     * 이벤트 등록 가능 여부 체크
     */
    public Boolean canCreateEvent(Long sellerId, String dateParam) {
        LocalDateTime inputDtm = LocalDateTime.parse(dateParam);
        eventRepository.findAllByStartAtBetweenAndIsDeleted(inputDtm, inputDtm.plusMinutes(50),
                false)
            .stream().filter(event -> event.getType().equals("FCFS"))
            .findAny().ifPresent(event -> {
                throw new ParaboleException(HttpStatus.ALREADY_REPORTED,
                    "선택하신 시간에 이미 등록된 이벤트가 있습니다.");
            });

        int dayOfWeek = inputDtm.getDayOfWeek().getValue();
        eventRepository.findAllByStartAtBetweenAndIsDeleted(inputDtm.minusDays(dayOfWeek),
                inputDtm.plusDays(6 - dayOfWeek), false)
            .stream()
            .filter(event -> event.getSellerId().equals(sellerId) && event.getType().equals("FCFS"))
            .findAny().ifPresent(event -> {
                throw new ParaboleException(HttpStatus.ALREADY_REPORTED,
                    "선착순 이벤트는 일주일에 하나만 등록 가능합니다.");
            });
        return true;
    }

    /**
     * 이벤트 취소
     */
    @Transactional(readOnly = false)
    public void cancelEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND, "해당 이벤트가 존재하지 않습니다."));
        try {
            event.cancel();

            event.getEventPrizes().forEach(e -> {
                if (e.getPrizeType().equals("PRODUCT")) {
                    log.info("cancel event no.{} & product no.{}", eventId, e.getProduct().getId());
                    if (!paraboleServiceClient.setProductRemains(e.getProduct().getId(), e.getStock())) {
                        throw new ParaboleException(HttpStatus.INTERNAL_SERVER_ERROR, "상품 수량 취소가 불가능합니다.");
                    };
                } else if (e.getPrizeType().equals("COUPON")) {
                    log.info("cancel event no.{} & coupon no.{}", eventId, e.getCoupon().getId());
                    if (!paraboleServiceClient.setCouponRemains(e.getCoupon().getId(), e.getStock())) {
                        throw new ParaboleException(HttpStatus.INTERNAL_SERVER_ERROR, "쿠폰 수량 취소가 불가능합니다.");
                    };
                }
            });

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
