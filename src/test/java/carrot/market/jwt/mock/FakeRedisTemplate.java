package carrot.market.jwt.mock;

import carrot.market.util.holder.RedisTemplateHolder;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
@Builder
public class FakeRedisTemplate implements RedisTemplateHolder {

    private String email;
    private String refreshToken;
    private long refreshExpirationTime;
    private static final long MILLI_SCALE  = 1000L * 1L;

    @Override
    public void saveRefreshToken(String refreshToken, String email, Long refreshExpirationTime) {
        this.email = email;
        this.refreshToken = refreshToken;
    }

    @Override
    public Optional<String> getRefreshToken(String username) {
        return Optional.empty();
    }
}
