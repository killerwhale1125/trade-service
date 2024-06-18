package carrot.market.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    public String create(String email) {

        Date expiredDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));

        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 헤더 설정
                .setSubject(email)  // 페이로드
                .setIssuedAt(new Date()).setExpiration(expiredDate) // 페이로드
                .compact();
        return jwt;
    }

    /**
     * 검증
     */
    public String validate(String jwt) {
        Claims claims = null;

        try {
            claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
        } catch(Exception exception) {
            exception.printStackTrace();
            return null;
        }

        return claims.getSubject();
    }
}
