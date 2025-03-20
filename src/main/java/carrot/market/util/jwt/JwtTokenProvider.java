package carrot.market.util.jwt;

import carrot.market.common.annotation.RedisTransactional;
import carrot.market.common.baseutil.BaseException;
import carrot.market.util.holder.DateHolder;
import carrot.market.util.holder.RedisTemplateHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

import static carrot.market.common.baseutil.BaseResponseStatus.TOKEN_ISEMPTY;
import static carrot.market.common.baseutil.BaseResponseStatus.TOKEN_MISMATCHED;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final RedisTemplateHolder redisTemplate;
    private final DateHolder dateHolder;

    /**
     * token 추출
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Refresh 토큰 생성 및 Redis 저장
     * RedisTransactional 로 예외 발생 시 Rollback 처리
     */
    @RedisTransactional
    public JwtToken generateToken(String email, Long memberId, HttpServletResponse response) {
        String refreshToken = createRefreshToken(email);
        /* Redis 저장 */
        redisTemplate.saveRefreshToken(refreshToken, email, jwtProperties.getToken().getRefreshExpirationTime());
        addRefreshTokenToCookie(refreshToken, response);

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(createAccessToken(email))
                .build();
    }

    private void addRefreshTokenToCookie(String refreshToken, HttpServletResponse response) {
        Cookie refreshCookie = new Cookie("refresh-token", refreshToken);

        refreshCookie.setHttpOnly(true);  // JavaScript로 접근 불가 (보안 설정)
//        refreshCookie.setSecure(true);    // HTTPS에서만 전송되도록 설정 (프로덕션 환경에서 보안 강화)
        refreshCookie.setPath("/");       // 쿠키가 유효한 경로 설정
        refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 쿠키 만료 시간 설정 (예: 7일)

        response.addCookie(refreshCookie);
    }

    public String validate(String token){
        return Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getSecretKey())
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * JWT 토큰 복호화
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * Access 토큰 생성
     */
    public String createAccessToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        Date now = dateHolder.createDate();
        Date expireDate = dateHolder.createExpireDate(now.getTime(), jwtProperties.getToken().getAccessExpirationTime());

        log.info("date.getTime() : {}", now.getTime());
        log.info("date.expireDate() : {}", expireDate.getTime());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    /**
     * Refresh 토큰 생성
     */
    @RedisTransactional
    public String createRefreshToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        Date now = dateHolder.createDate();
        Date expireDate = dateHolder.createExpireDate(now.getTime(), jwtProperties.getToken().getRefreshExpirationTime());

        /**
         * Refresh 토큰 생성
         */
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();

        redisTemplate.saveRefreshToken(refreshToken, email, jwtProperties.getToken().getRefreshExpirationTime());

        return refreshToken;
    }

    public JwtToken reissueRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Refresh 토큰 검증
         * 토큰 값이 존재하는데 검증 실패 시 재 로그인 필요
         */
        String requestRefreshToken = resolveToken(request);
        String email = validate(requestRefreshToken);

        /**
         * Redis에 저장된 Refresh 토큰 조회
         * Refresh 만료 시 Redis에 자동 삭제되기 때문에 서버 측 검증 X
         * null -> 만료
         */
        String redisRefreshToken = redisTemplate
                .getRefreshToken(email)
                .orElseThrow(() -> new BaseException(TOKEN_ISEMPTY));

        /**
         * Refresh 토큰 null 체크 & 동등성 여부 판단
         */
        if(!StringUtils.hasText(redisRefreshToken) || !requestRefreshToken.equals(redisRefreshToken)) {
            throw new BaseException(TOKEN_MISMATCHED);
        }

        /*
         - Redis 재발급
         - Redis에 저장
         - 쿠키 저장
         */
        String refreshToken = createRefreshToken(email);
        redisTemplate.saveRefreshToken(refreshToken, email, jwtProperties.getToken().getRefreshExpirationTime());
        addRefreshTokenToCookie(createRefreshToken(email), response);

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(createAccessToken(email))
                .build();
    }

}