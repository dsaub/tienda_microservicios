package me.elordenador.tienda.auth.security;

import lombok.RequiredArgsConstructor;
import me.elordenador.tienda.auth.model.User;
import me.elordenador.tienda.auth.repository.UserRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class PasswordVersionCacheService {
    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private static final String KEY_PREFIX  ="user:pwd_ver:";

    public int getCurrentVersion(Long userId) {
        String key = KEY_PREFIX + userId;
        String cached = redisTemplate.opsForValue().get(key);

        if (cached != null) {
            return Integer.parseInt(cached);
        }

        int dbVersion = userRepository.findById(userId)
                .map(User::getPasswordVersion)
                .orElse(-1);
        if (dbVersion != -1) {
            redisTemplate.opsForValue().set(key, String.valueOf(dbVersion), Duration.ofHours(24));
        }
        return dbVersion;
    }

    public void updateVersion(Long userId, int newVersion) {
        redisTemplate.opsForValue().set(KEY_PREFIX + userId, String.valueOf(newVersion), Duration.ofHours(24));
    }
}
