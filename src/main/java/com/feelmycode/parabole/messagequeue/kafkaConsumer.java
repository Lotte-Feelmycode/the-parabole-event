package com.feelmycode.parabole.messagequeue;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feelmycode.parabole.dto.EventApplyDto;
import com.feelmycode.parabole.repository.EventParticipantRepository;
import com.feelmycode.parabole.service.EventParticipantService;
import java.util.HashMap;
import java.util.Map;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class kafkaConsumer {

    EventParticipantRepository eventParticipantRepository;
    EventParticipantService eventParticipantService;

    @Autowired
    public kafkaConsumer(EventParticipantRepository eventParticipantRepository,
        EventParticipantService eventParticipantService) {
        this.eventParticipantRepository = eventParticipantRepository;
        this.eventParticipantService = eventParticipantService;
    }
    @Transactional
    @KafkaListener(topics = "v10-event-topic",groupId = "GroupEvent")
    public void updateQty(String kafkaMessage) {
        log.info("kafka Message : =>" + kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
            });
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        EventApplyDto applyDto = new EventApplyDto(ObjectToLong(map.get("userId")),
            ObjectToLong(map.get("eventId")), ObjectToLong(map.get("eventPrizeId")));

        eventParticipantService.eventJoin(applyDto);
    }

    private Long ObjectToLong(Object object) {
        return Long.valueOf(String.valueOf(object));
    }

}
