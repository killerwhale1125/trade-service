package carrot.market.util.system;

import carrot.market.util.holder.RedisTemplateHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class SystemRedisTemplate implements RedisTemplateHolder {
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void saveRefreshToken(String refreshToken, String email, Long refreshExpirationTime) {
        redisTemplate.opsForValue().set(
                email,
                refreshToken,
                refreshExpirationTime,
                TimeUnit.MILLISECONDS);
    }

    @Override
    public Optional<String> getRefreshToken(String username) {
        return Optional.of(redisTemplate.opsForValue().get(username));
    }
}
