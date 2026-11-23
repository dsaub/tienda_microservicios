package me.elordenador.tienda.auth.messaging;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.List;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailQueuePublisher {

    private static final String QUEUE = "alarm_queue";
    private static final String MESSAGE_KEY_PREFIX = "message:email: ";
    private static final Duration MESSAGE_TTL = Duration.ofDays(7);

    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper;

    private static final DefaultRedisScript<Long> ENQUEUE = new DefaultRedisScript<>("""
        redis.call('HSET', KEYS[1], 'type', ARGV[2], 'locale', ARGV[3], 'variables', ARGV[4])
        redis.call('EXPIRE', KEYS[1], ARGV[5])
        redis.call('LPUSH', KEYS[2], ARGV[1])
        return 1
        """, Long.class);

    public void publish(String messageId, long recipientId, EmailType type, String locale, String variablesJson) {
        Objects.requireNonNull(messageId, "messageId");
        Objects.requireNonNull(type, "type");

        String messageKey = MESSAGE_KEY_PREFIX + messageId;

        Map<String, Object> pointer = Map.of(
                "id", messageId,
                "recipient_id", recipientId,
                "type", "EMAIL"
        );

        String pointerJson;

        try {
            pointerJson = objectMapper.writeValueAsString(pointer);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot initialize pointer " + messageId, e);
        }

        redis.execute(ENQUEUE,
                List.of(messageKey, QUEUE),
                pointerJson, type.name(), locale, variablesJson,
                String.valueOf(MESSAGE_TTL.toSeconds()));
    }
}
