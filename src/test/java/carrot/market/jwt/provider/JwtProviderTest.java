package carrot.market.jwt.provider;

import carrot.market.jwt.mock.FakeAuthentication;
import carrot.market.jwt.mock.FakeDate;
import carrot.market.jwt.mock.FakeRedisTemplate;
import carrot.market.util.holder.RedisTemplateHolder;
import carrot.market.util.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Date;

@Slf4j
public class JwtProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        Date fixedDate = new Date();
        Date fixedExpireTime = new Date(fixedDate.getTime() + 1800000);

        FakeDate fakeDate = new FakeDate(fixedDate, fixedExpireTime);
        FakeRedisTemplate redisTemplate = FakeRedisTemplate.builder().refreshExpirationTime(fixedExpireTime.getTime()).build();

        this.jwtTokenProvider = JwtTokenProvider.builder()
                .redisTemplate(redisTemplate)
                .dateHolder(fakeDate)
                .key("bXlTZWNyZXRLZbXlTZWNyZXRLZbXlTZWNyZXRLZbXlTZWNyZXRLZ")
                .build();
    }

    @Test
    @DisplayName("Authorization Header에서 토큰 추출 성공")
    void resolveToken_Success() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer tokenValuezzzzzzzzzzzzzzz");
        // when
        String token = jwtTokenProvider.resolveToken(request);
        // then
        Assertions.assertThat(token).isEqualTo("tokenValuezzzzzzzzzzzzzzz");
    }

    @Test
    @DisplayName("Authorization Header에서 토큰 추출 실패")
    void resolveToken_Fail() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "");
        // when
        String token = jwtTokenProvider.resolveToken(request);
        // then
        Assertions.assertThat(token).isEqualTo(null);
    }

    @Test
    @DisplayName("Access 토큰 생성 테스트")
    void createAccessToken() throws Exception {
        // given
        FakeAuthentication authentication = new FakeAuthentication("killerwhale@naver.com");
        FakeDate fakeDate = (FakeDate) jwtTokenProvider.getDateHolder();
        Date date = fakeDate.getDate();
        Date expireDate = fakeDate.getExpireDate();
        String key = jwtTokenProvider.getKey();

        // when
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken).getBody();
        // then
        /**
         * 1. claims의 subject가 killerwhale이 맞는지?
         * 2. Date가 정해진 시간으로 설정되었는지?
         * 3. expireDate가 정해진 시간으로 설정되었는지?
         */
        Assertions.assertThat(claims.getSubject()).isEqualTo("killerwhale@naver.com");
        Assertions.assertThat(claims.getIssuedAt().getTime() / 1000).isEqualTo(date.getTime() / 1000);
        Assertions.assertThat(claims.getExpiration().getTime() / 1000).isEqualTo(expireDate.getTime() / 1000);
    }

    @Test
    @DisplayName("Refresh 토큰 생성 테스트")
    void createRefreshToken() throws Exception {
        // given
        FakeAuthentication authentication = new FakeAuthentication("killerwhale@naver.com");

        FakeDate fakeDate = (FakeDate) jwtTokenProvider.getDateHolder();
        Date date = fakeDate.getDate();
        Date expireDate = fakeDate.getExpireDate();
        String key = jwtTokenProvider.getKey();

        // when
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(refreshToken).getBody();

        // then
        /**
         * 1. claims의 subject가 killerwhale이 맞는지?
         * 2. Date가 정해진 시간으로 설정되었는지?
         * 3. expireDate가 정해진 시간으로 설정되었는지?
         */
        Assertions.assertThat(claims.getSubject()).isEqualTo("killerwhale@naver.com");
        Assertions.assertThat(claims.getIssuedAt().getTime() / 1000).isEqualTo(date.getTime() / 1000);
        Assertions.assertThat(claims.getExpiration().getTime() / 1000).isEqualTo(expireDate.getTime() / 1000);

        // redis
        FakeRedisTemplate redisTemplate = (FakeRedisTemplate) jwtTokenProvider.getRedisTemplate();
        Assertions.assertThat(redisTemplate.getRefreshToken()).isEqualTo(refreshToken);
        Assertions.assertThat(redisTemplate.getEmail()).isEqualTo("killerwhale@naver.com");
        Assertions.assertThat(redisTemplate.getRefreshExpirationTime() / 1000).isEqualTo(expireDate.getTime() / 1000);
    }

}
