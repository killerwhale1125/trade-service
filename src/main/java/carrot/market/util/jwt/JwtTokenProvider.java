package carrot.market.util.jwt;

import carrot.market.common.annotation.RedisTransactional;
import carrot.market.common.baseutil.BaseException;
import carrot.market.common.baseutil.BaseResponseStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${security.jwt.secret-key}")
    private String key;

    @Value("${security.jwt.token.access-expiration-time}")
    private long accessExpirationTime;

    @Value("${security.jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * token 추출
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Authentication 정보로 AccessToken, RefreshToken을 생성
     */
    public JwtToken generateToken(Authentication authentication) {

        /**
         * Access와 Refresh 토큰을 생성.
         * Expire Time은 yml 설정 파일로 관리
         * Login 마다 토큰 발급
         */
        String accessToken = createAccessToken(authentication);
        String refreshToken = createRefreshToken(authentication);

        JwtToken bearer = JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return bearer;
    }

    /**
     * 주어진 Access token을 복호화하여 사용자의 인증 정보(Authentication)를 생성
     * 토큰의 Claims에서 권한 정보를 추출하고, User 객체를 생성하여 Authentication 객체로 반환
     * Collection<? extends GrantedAuthority>로 리턴받는 이유
     * 권한 정보를 다양한 타입의 객체로 처리할 수 있고, 더 큰 유연성과 확장성을 가질 수 있음
     * [ Authentication 객체 생성하는 과정 ]
     *
     * 토큰의 클레임에서 권한 정보를 가져옴. "auth" 클레임은 토큰에 저장된 권한 정보를 나타냄
     * 가져온 권한 정보를 SimpleGrantedAuthority 객체로 변환하여 컬렉션에 추가
     * UserDetails 객체를 생성하여 주체(subject)와 권한 정보, 기타 필요한 정보를 설정
     * UsernamepasswordAuthenticationToken 객체를 생성하여 주체와 권한 정보를 포함한 인증(Authentication) 객체를 생성
     */
    public Authentication getAuthentication(String accessToken) {
        // Jwt 토큰 복호화
        Claims claims = parseClaims(accessToken);

        // 사용자 인증만을 위해 authorities를 빈 리스트로 설정
        List<GrantedAuthority> authorities = Collections.emptyList();

        // UserDetails 객체를 만들어서 Authentication return
        // UserDetails: interface, User: UserDetails를 구현한 class
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public String validate(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * JWT 토큰 복호화
     */
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * Access 토큰 생성
     */
    public String createAccessToken(Authentication authentication){
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessExpirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    /**
     * Refresh 토큰 생성
     */
    @RedisTransactional
    public String createRefreshToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshExpirationTime);

        /**
         * Refresh 토큰 생성
         */
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        redisTemplate.opsForValue().set(
                authentication.getName(),
                refreshToken,
                refreshExpirationTime,
                TimeUnit.MILLISECONDS);

        return refreshToken;
    }

    /**
     * Refresh 토큰을 사용하여 재발급
     */
    public JwtToken refreshTokens(String refreshToken) {
        /**
         * Refresh 토큰 검증
         * 토큰 값이 존재하는데 검증 실패 시 재 로그인 필요
         */
        validate(refreshToken);
        
        Claims claims = parseClaims(refreshToken);
        String username = claims.getSubject();

        /**
         * Redis에 저장된 Refresh 토큰 조회
         * Refresh 만료 시 Redis에 자동 삭제되기 때문에 서버 측 검증 X
         * null -> 만료
         */
        String redisRefreshToken = redisTemplate.opsForValue().get(username);

        /**
         * Refresh 토큰 null 체크 & 동등성 여부 판단
         */
        if(!StringUtils.hasText(redisRefreshToken) || !refreshToken.equals(redisRefreshToken)) {
            throw new BaseException(BaseResponseStatus.TOKEN_MISMATCHED);
        }

        /**
         * 토큰에 담긴 유저 정보로 Authentication 객체 조회
         * 보안을 고려하여 Refresh 토큰도 만료되지 않았더라도 재발급
         */
        Authentication authentication = getAuthenticationFromClaims(claims);
        String newAccessToken = createAccessToken(authentication);
        String newRefreshToken = createRefreshToken(authentication);

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    /**
     * Token 정보로 Authentication 객체 생성
     */
    private Authentication getAuthenticationFromClaims(Claims claims) {
        String username = claims.getSubject();

        // 사용자 인증만을 위해 authorities를 빈 리스트로 설정
        List<GrantedAuthority> authorities = Collections.emptyList();

        UserDetails principal = new User(username, "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
}