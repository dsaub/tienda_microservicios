package me.elordenador.tienda.auth.services;

import lombok.RequiredArgsConstructor;
import me.elordenador.tienda.auth.messaging.EmailQueuePublisher;
import me.elordenador.tienda.auth.messaging.EmailType;
import me.elordenador.tienda.auth.model.User;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailQueuePublisher publisher;
    private final ObjectMapper objectMapper;
    public void sendConfirmationEmail(User user) {
        String confirmUrl = buildConfirmUrl(user);
        Map<String, String> variables = Map.of(
                "username", user.getUsername(),
                "confirm_url", confirmUrl
        );

        String variablesJson;
        try {
            variablesJson = objectMapper.writeValueAsString(variables);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot initialize variables");
        }

        publisher.publish(
                UUID.randomUUID().toString(),
                user.getId(),
                EmailType.USER_CONFIRMATION,
                user.getLocale(),
                variablesJson
        );
    }

    private String buildConfirmUrl(User user) {
        return "https://tienda.example/confirm?token=" + UUID.randomUUID();
    }
}
