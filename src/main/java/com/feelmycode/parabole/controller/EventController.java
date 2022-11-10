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

    @PostMapping
    public ResponseEntity<ParaboleResponse> createEvent(
        @RequestBody @Valid EventCreateRequestDto eventDto) {
        Long eventId = -1L;
        try {
            eventId = eventService.createEvent(eventDto);
        } catch (Exception e) {
            throw new ParaboleException(HttpStatus.INTERNAL_SERVER_ERROR, "이벤트 등록 실패");
        }
        return ParaboleResponse.CommonResponse(HttpStatus.CREATED, true, "이벤트 등록 성공", eventId);
    }

    // TODO: 셀러 스토어 정보 리턴값 추가
    @GetMapping("/{eventId}")
    public ResponseEntity<ParaboleResponse> getEvent(@PathVariable("eventId") Long eventId) {
        EventListResponseDto response = eventService.getEventByEventId(eventId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, eventId + "번 이벤트 조회 성공",
            response);
    }

    // TODO: 조회조건+정렬조건 추가
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
        @RequestParam(required = false) LocalDateTime fromDateTime,
        @RequestParam(required = false) LocalDateTime toDateTime,
        @RequestParam(required = false) Integer eventStatus
    ) {
        Integer getDateDiv = StringUtil.controllerParamIsBlank(dateDiv + "") ? -1 : dateDiv;
        String getEventType = StringUtil.controllerParamIsBlank(eventType) ? "" : eventType;
        String getEventTitle = StringUtil.controllerParamIsBlank(eventTitle) ? "" : eventTitle;
        Integer getEventStatus =
            StringUtil.controllerParamIsBlank(eventStatus + "") ? -1 : eventStatus;

        List<EventSearchResponseDto> response = eventService.getEventsSearch(
            getEventType, getEventTitle, getDateDiv, fromDateTime, toDateTime, getEventStatus
        );
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "이벤트 검색 리스트 조회 성공", response);
    }

    // TODO: 기존에 heyheya<3 님이 만드신 이벤트 리스트 조회 '원본' API 입니다.
    @GetMapping("/seller/{userId}")
    public ResponseEntity<ParaboleResponse> getEventByUserId(@PathVariable("userId") Long userId) {
        List<EventListResponseDto> response = eventService.getEventListResponseDto(
            eventService.getEventsBySellerId(userId));
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "이벤트 리스트 조회 성공", response);
    }

    // TODO: PR 때 팀원들이 확인하고 merge 이전에 삭제해야할 테스트용 API
    // TODO: Jwt token이 market -> gateway -> event 이동 후 event server API 에서 인식이 잘 되는지 테스트 하기 위해
    // 만들었어요. 바로 위에 함수가 해당 테스트 함수로 변경되면 인증과 API 접근이 됩니다.
    @GetMapping("/seller")
    public ResponseEntity<ParaboleResponse> getEventByUserIdTest(@RequestAttribute Long userId) {
        List<EventListResponseDto> response = eventService.getEventListResponseDto(
            eventService.getEventsBySellerId(userId));
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "이벤트 리스트 조회 성공", response);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<ParaboleResponse> cancelEvent(@PathVariable("eventId") Long eventId) {
        try {
            eventService.cancelEvent(eventId);
        } catch (Exception e) {
            throw new ParaboleException(HttpStatus.INTERNAL_SERVER_ERROR, "이벤트 취소 실패");
        }
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "이벤트 취소 성공");
    }
}
