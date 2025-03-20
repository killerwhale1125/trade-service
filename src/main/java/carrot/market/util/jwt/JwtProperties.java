package carrot.market.util.jwt;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Validated
@ConfigurationProperties("security.jwt")
public class JwtProperties {
    @NotEmpty
    private final String secretKey;
    @NotNull
    private final Token token;

    public JwtProperties(String secretKey, Token token) {
        this.secretKey = secretKey;
        this.token = token;
    }

    @Getter
    static class Token {
        @Min(1800)  // 최소 30분
        private final long accessExpirationTime;
        @Min(1209600)   // 최소 2주
        private final long refreshExpirationTime;

        public Token(long accessExpirationTime, long refreshExpirationTime) {
            this.accessExpirationTime = accessExpirationTime;
            this.refreshExpirationTime = refreshExpirationTime;
        }
    }
}
