package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.service.EventParticipantService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventParticipantTest {

    @Autowired
    EventParticipantService eventParticipantService;

    @Test
    public void test01() {
        Long eventId = 1L;
        eventParticipantService.eventFCFSWinner(eventId);
    }

    @Test
    public void test02(){
        Long eventId=3L;
        eventParticipantService.eventRaffleStart(eventId);
    }
}
