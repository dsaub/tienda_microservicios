package me.elordenador.tienda.auth.messaging;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailQueuePublisher {

    private static final String QUEUE = "alarm_queue";
    private static final String MESSAGE_KEY_PREFIX = "message:email: ";
    private static final Duration MESSAGE_TTL = Duration.ofDays(7);

    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper;

    public void publish(String messageId, long senderId, Map<String, String> content) {
        String messageKey = MESSAGE_KEY_PREFIX + messageId;

        Map<String, String> hash = new LinkedHashMap<>(content);
        redis.opsForHash().putAll(messageKey, hash);
        redis.expire(messageKey, MESSAGE_TTL);

        Map<String, Object> pointer = Map.of(
                "id", messageId,
                "sender_id", senderId,
                "type", "EMAIL"
        );

        redis.opsForList().leftPush(QUEUE, objectMapper.writeValueAsString(pointer));
    }
}
