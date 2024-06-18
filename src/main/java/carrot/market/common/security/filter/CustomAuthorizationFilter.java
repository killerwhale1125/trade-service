package carrot.market.common.security.filter;

import carrot.market.common.security.auth.PrincipalDetails;
import carrot.market.util.jwt.JwtProvider;
import carrot.market.entity.member.Member;
import carrot.market.repository.member.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 토큰 검증
 * 사용자가 요청한 리소스에 접근할 권한이 있는지 확인하는 과정
 * 로그인 이후의 모든 요청에 대해 JWT 토큰을 확인하고, 사용자가 요청한 리소스에 접근할 권한이 있는지 확인.
 */
@Component
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {

            String token = parseBearerToken(request);

            if(token == null) {
                // 토큰 없을 시 다음 필터로 넘김
                filterChain.doFilter(request, response);
                return;
            }

            String email = jwtProvider.validate(token);

            if(email == null) {
                // 이메일 없을 시 다음 필터로 넘김
                filterChain.doFilter(request, response);
                return;
            }

            Member member = memberRepository.findByEmail(email).orElseThrow();

            PrincipalDetails principalDetails = new PrincipalDetails(member);
            // Authentication 인증 객체 생성
            AbstractAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(principalDetails, null, AuthorityUtils.NO_AUTHORITIES);

            // 웹인증 세부정부 소스 -> Client IP, Session ID 를 WebAuthenticationDetails 에 생성하여 세부정보를 인증객체에 저장
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch(Exception exception) {
            exception.printStackTrace();
        }

        filterChain.doFilter(request, response);
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
