package com.feelmycode.parabole.messagequeue;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
            "3.35.139.139:9092,43.201.29.30:9092,13.209.80.189:9092");
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic newTopic(){
        return new NewTopic("v13-event-topic",3,(short)3);
    }
}
