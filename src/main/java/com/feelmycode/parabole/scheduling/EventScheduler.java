package com.feelmycode.parabole.scheduling;

import com.feelmycode.parabole.repository.EventRepository;
import com.feelmycode.parabole.service.EventService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor

@Component
public class EventScheduler {

    private final EventService eventService;

    @Scheduled(cron = "0 0 * * * *")// 앞에서 부터 초 분 시 일 월 요일
    public void EventStatusScheduling() {
        System.out.println("이벤트 시작");
        eventService.updateEventStatus(LocalDateTime.now());
        log.info("event scheduling date=>");
    }

    @Scheduled(cron = "0 50 * * * *")
    public void EventDrawStart() {
        eventService.eventFCFSStart(LocalDateTime.now());
    }

}
