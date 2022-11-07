package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.dto.EventWinnerDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.service.EventWinnerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
@RestController
public class EventWinnerController {

    private final EventWinnerService eventWinnerService;

    @GetMapping("/seller/eventwinner/list/{eventId}")
    public ResponseEntity<ParaboleResponse> getSellerEventWinnerList(@PathVariable Long eventId) {
        List<EventWinnerDto> response = eventWinnerService.eventWinnerList(eventId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "셀러 이벤트 응모자 리스트 조회", response);
    }

}
