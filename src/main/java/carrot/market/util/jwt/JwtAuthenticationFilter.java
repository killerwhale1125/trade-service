package carrot.market.util.jwt;

import carrot.market.common.baseutil.BaseResponse;
import carrot.market.common.baseutil.BaseResponseStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;

import static carrot.market.common.baseutil.BaseResponseStatus.*;
import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            // 1. Request Header에서 JWT 토큰 추출
            String token = parseBearerToken(request);

            if(!StringUtils.hasText(token)) {
                // 토큰 없을 시 다음 필터로 넘김
                chain.doFilter(request, response);
                return;
            }

            String email = jwtTokenProvider.validate(token);

            if(!StringUtils.hasText(email)) {
                // 이메일 없을 시 다음 필터로 넘김
                chain.doFilter(request, response);
                return;
            }

            // Authentication 인증 객체 생성
            AbstractAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(email, null, AuthorityUtils.NO_AUTHORITIES);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception e) {
            handleJwtException(e, response);
            return;
        }
        chain.doFilter(request, response);
    }

    public void setResponse(HttpServletResponse response, BaseResponseStatus baseResponseStatus) throws IOException {
        response.setStatus(SC_BAD_REQUEST);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        BaseResponse<Void> baseResponse = new BaseResponse<>(baseResponseStatus);
        new ObjectMapper().writeValue(response.getWriter(), baseResponse);
    }

    private void handleJwtException(Exception e, HttpServletResponse response) throws IOException {
        BaseResponseStatus status;

        if (e instanceof SecurityException || e instanceof MalformedJwtException) {
            status = INVALID_TOKEN;
        } else if (e instanceof ExpiredJwtException) {
            status = EXPIRED_TOKEN;
        } else if (e instanceof UnsupportedJwtException) {
            status = UNSUPPORTED_TOKEN;
        } else if (e instanceof IllegalArgumentException) {
            status = TOKEN_ISEMPTY;
        } else if (e instanceof SignatureException) {
            status = INVALID_TOKEN;
        } else {
            status = INVALID_TOKEN;
        }

        logException(e, status);
        setResponse(response, status);
    }

    private void logException(Exception e, BaseResponseStatus status) {
        log.debug("JWT Exception: {}", status, e);
        log.error("JWT Exception: {}", status, e);
    }

    private String parseBearerToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        // null or 공백 or 길이 0 일 경우 false
        if(!StringUtils.hasText(authorization))
            return null;

        if(!authorization.startsWith("Bearer "))
            return null;

        // 토큰 반환
        return authorization.substring(7);
    }

}