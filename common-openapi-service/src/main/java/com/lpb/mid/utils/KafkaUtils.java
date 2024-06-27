package com.lpb.mid.utils;

import com.lpb.mid.dto.SendKafkaDto;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;

import java.util.UUID;

public class KafkaUtils {
   public static RequestReplyFuture<String, Object, Object> sendMessageKafka(SendKafkaDto sendKafkaDto, String topicSend, String topicReply, ReplyingKafkaTemplate<String, Object, Object> replyingTemplate) {
        RequestReplyFuture<String, Object, Object> replyFuture;
        ProducerRecord<String, Object> record = new ProducerRecord<>(topicSend, Constants.Authorization + sendKafkaDto.getUsername() + UUID.randomUUID(), sendKafkaDto);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, topicReply.getBytes()));
        record.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes()));
        replyFuture = replyingTemplate.sendAndReceive(record);
        return replyFuture;
    }
}
