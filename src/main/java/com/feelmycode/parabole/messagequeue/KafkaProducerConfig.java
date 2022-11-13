package com.feelmycode.parabole.messagequeue;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@EnableKafka
@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
            "3.35.139.139:9092,43.201.29.30:9092,13.209.80.189:9092"
        );
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG,"30000");//기본 batchsize 16384
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,true);//중복없는 전송을 위한 처리(멱등성 전송)
        properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,2);
        properties.put(ProducerConfig.LINGER_MS_CONFIG,10);
        properties.put(ProducerConfig.ACKS_CONFIG,"all");
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,"gzip");//메세지 압축방식

       // properties.put("buffer.memory", 24568545);
        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
