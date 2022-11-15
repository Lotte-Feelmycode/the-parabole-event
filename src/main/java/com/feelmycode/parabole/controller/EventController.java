package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.dto.EventCreateRequestDto;
import com.feelmycode.parabole.dto.EventListResponseDto;
import com.feelmycode.parabole.dto.EventSearchResponseDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.global.util.StringUtil;
import com.feelmycode.parabole.service.EventService;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
@RestController
public class EventController {

    private final EventService eventService;

    // TODO : AWS service
    @PostMapping
    public ResponseEntity<ParaboleResponse> createEvent(
        @RequestAttribute("sellerId") Long sellerId,
        @RequestBody @Valid EventCreateRequestDto eventDto) {
        Long eventId = -1L;
        if (!eventService.canCreateEvent(sellerId, eventDto.getStartAt().toString())) {
            throw new ParaboleException(HttpStatus.ALREADY_REPORTED, "이벤트 등록 실패");
        }
        try {
            eventId = eventService.createEvent(sellerId, eventDto);
        } catch (Exception e) {
            throw new ParaboleException(HttpStatus.INTERNAL_SERVER_ERROR, "이벤트 등록 실패");
        }
        return ParaboleResponse.CommonResponse(HttpStatus.CREATED, true, "이벤트 등록 성공", eventId);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ParaboleResponse> getEvent(@PathVariable("eventId") Long eventId) {
        EventListResponseDto response = eventService.getEventByEventId(eventId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, eventId + "번 이벤트 조회 성공",
            response);
    }

    @GetMapping
    public ResponseEntity<ParaboleResponse> getEvent() {
        List<EventListResponseDto> response = eventService.getEventListResponseDto(
            eventService.getEventsAllNotDeleted());
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "이벤트 리스트 조회 성공", response);
    }

    @GetMapping("/list")
    public ResponseEntity<ParaboleResponse> getEventList(
        @RequestParam(required = false) String eventType,
        @RequestParam(required = false) String eventTitle,
        @RequestParam(required = false) Integer dateDiv,
        @RequestParam(required = false) String fromDateTime,
        @RequestParam(required = false) String toDateTime,
        @RequestParam(required = false) Integer eventStatus,
        @RequestAttribute(required = false) Long sellerId
    ) {
        Integer getDateDiv = StringUtil.controllerParamIsBlank(dateDiv + "") ? -1 : dateDiv;
        String getEventType = StringUtil.controllerParamIsBlank(eventType) ? "" : eventType;
        String getEventTitle = StringUtil.controllerParamIsBlank(eventTitle) ? "" : eventTitle;
        Integer getEventStatus =
            StringUtil.controllerParamIsBlank(eventStatus + "") ? -1 : eventStatus;
        LocalDateTime getFromDateTime = StringUtil.controllerParamIsBlank(fromDateTime) ? null : LocalDateTime.parse(fromDateTime);
        LocalDateTime getToDateTime = StringUtil.controllerParamIsBlank(toDateTime) ? null : LocalDateTime.parse(fromDateTime);

        List<EventSearchResponseDto> response = eventService.getEventsSearch(
            getEventType, getEventTitle, getDateDiv, getFromDateTime, getToDateTime, getEventStatus, sellerId
        );
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "이벤트 검색 리스트 조회 성공", response);
    }

    @GetMapping("/seller")
    public ResponseEntity<ParaboleResponse> getEventBySellerId(@RequestAttribute Long sellerId) {
        List<EventListResponseDto> response = eventService.getEventListResponseDto(
            eventService.getEventsBySellerId(sellerId));
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "이벤트 리스트 조회 성공", response);
    }

    @GetMapping("/seller/scheduler")
    public ResponseEntity<ParaboleResponse> getEventScheduler() {
        List<EventSearchResponseDto> response = eventService.getEventsMonthAfter();
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "이벤트 스케쥴러 조회 성공", response);
    }

    @GetMapping("/seller/check")
    public ResponseEntity<ParaboleResponse> showIsAvailable(
        @RequestAttribute("sellerId") Long sellerId, @RequestParam("inputDtm") String inputDate) {
        if (!eventService.canCreateEvent(sellerId, inputDate)) {
            return ParaboleResponse.CommonResponse(HttpStatus.ALREADY_REPORTED, true, "이벤트 등록 가능", false);
        } else {
            return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "이벤트 등록 가능", true);
        }
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<ParaboleResponse> cancelEvent(@RequestAttribute("sellerId") Long sellerId, @PathVariable("eventId") Long eventId) {
        try {
            eventService.cancelEvent(eventId);
        } catch (Exception e) {
            throw new ParaboleException(HttpStatus.INTERNAL_SERVER_ERROR, "이벤트 취소 실패");
        }
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "이벤트 취소 성공");
    }
}
