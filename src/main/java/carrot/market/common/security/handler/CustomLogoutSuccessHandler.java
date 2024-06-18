package carrot.market.common.security.handler;

import carrot.market.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static carrot.market.common.HttpStatusResponseEntity.RESPONSE_OK;

//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
//    // logoutHandler는 예외가 발생하지 않는다 -> 처리기 중 일부 캐시를 정리를 수행할 수 있기 때문에 해당 메서드는 성공적으로 완료되어야한다.
//    private final MemberService memberService;
//    private final JwtService jwtService;
//
//    @Override
//    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, IOException {
//        Long memberId = jwtService.getMemberId();
//        memberService.removeRefreshToken(memberId);
//        log.info("refresh 토큰 삭제 완료");
//
//        new ObjectMapper().writeValue(response.getWriter(), RESPONSE_OK);
//    }
//}