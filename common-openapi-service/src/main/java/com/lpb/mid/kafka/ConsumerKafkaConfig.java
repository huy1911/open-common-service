package com.lpb.mid.kafka;

import com.lpb.mid.utils.Constants;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConsumerKafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;


    @Bean(name = "consumerCusFactory")
    public ConsumerFactory<String, String> consumerCusFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerCusConfigs(), new StringDeserializer(), new StringDeserializer());
    }

    // Concurrent Listner container factory
    @Bean(name = "kafkaListenerCusContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerCusContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerCusFactory());
        factory.setReplyTemplate(replyCusTemplate());
        return factory;
    }

    @Bean(name = "producerCusFactory")
    public ProducerFactory<String, String> producerCusFactory() {
        return new DefaultKafkaProducerFactory<>(producerCusConfigs());
    }


    @Bean(name = "replyCusTemplate")
    public KafkaTemplate<String, String> replyCusTemplate() {
        return new KafkaTemplate<>(producerCusFactory());
    }
    @Bean(name = "consumerCusConfigs")
    public Map<String, Object> consumerCusConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupId");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "20971520");
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, "20971520");

        return props;
    }
    @Bean(name = "producerCusConfigs")
    public Map<String, Object> producerCusConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, Constants.ALL);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 3000000);
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 3000000);
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "20971520");
        return props;
    }
    @Bean
    public KafkaListenerErrorHandler voidSendToErrorHandler() {
        return (m, e) -> "FAILED";
    }
}
