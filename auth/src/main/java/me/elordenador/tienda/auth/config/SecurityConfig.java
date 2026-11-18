package me.elordenador.tienda.auth.config;

import me.elordenador.tienda.auth.annotations.PublicEndpoint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            @Qualifier("requestMappingHandlerMapping")RequestMappingHandlerMapping mapping
            ) throws Exception {
        List<String> publicPaths = new ArrayList<>(List.of("/actuator/health/**"));
        mapping.getHandlerMethods().forEach((info, handlerMethod) -> {
            boolean isPublic = handlerMethod.hasMethodAnnotation(PublicEndpoint.class)
                    || handlerMethod.getBeanType().isAnnotationPresent(PublicEndpoint.class);

            if (isPublic && info.getPathPatternsCondition() != null) {
                publicPaths.addAll(info.getPathPatternsCondition().getPatternValues());
            }
        });

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicPaths.toArray(new String[0])).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

}
