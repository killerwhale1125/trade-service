package carrot.market.util.holder;

import java.util.List;
import java.util.Optional;

public interface RedisTemplateHolder {
    void saveRefreshToken(String refreshToken, String email, Long refreshExpirationTime);

    Optional<String> getRefreshToken(String username);
}
