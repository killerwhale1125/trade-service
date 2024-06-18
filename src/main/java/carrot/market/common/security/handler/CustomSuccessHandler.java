package carrot.market.common.security.handler;

import carrot.market.common.security.auth.PrincipalDetails;
import carrot.market.repository.member.MemberRepository;
import carrot.market.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//@RequiredArgsConstructor
//@Component
//@Slf4j
//@Primary
//public class CustomSuccessHandler implements AuthenticationSuccessHandler {
//
//    private final MemberService memberService;
//    private final MemberRepository memberRepository;
//
//    /**
//     *
//     * @param request the request which caused the successful authentication
//     * @param response the response
//     * @param authentication the <tt>Authentication</tt> object which was created during
//     * the authentication process.
//     * @throws IOException
//     */
//    @Override
//    public void onAuthenticationSuccess(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            Authentication authentication) throws IOException {
//        PrincipalDetails member = (PrincipalDetails) authentication.getPrincipal();
//
//        String accessToken = generateToken(member, true, AT_EXP_TIME);
//        String refreshToken = generateRefreshToken(member, true, RT_EXP_TIME);
//
//        // Refresh Token DB에 저장
//        memberService.updateRefreshToken(member.getUsername(), refreshToken);
//        response.setContentType(APPLICATION_JSON_VALUE);
//        response.setCharacterEncoding("utf-8");
//        response.addHeader("Set-Cookie", createCookie(refreshToken).toString());
//
//        Map<String, String> responseMap = new HashMap<>();
//        responseMap.put(AT_HEADER, accessToken);
//        responseMap.put("nickname", member.getMember().getNickname());
//
//        if(memberImage == null)
//            responseMap.put("profileImage", "");
//        else
//            responseMap.put("profileImage", memberImage.getSavedPath());
//
//        responseMap.put(AT_HEADER, accessToken);
//        responseMap.put("userId", String.valueOf(member.getMember().getId()));
//        new ObjectMapper().writeValue(response.getWriter(), new BaseResponse<>(responseMap));
//    }
//
//    public static ResponseCookie createCookie(String refreshToken) {
//
//        ResponseCookie cookie = ResponseCookie.from(RT_HEADER, refreshToken)
//                .path("/")
//                .sameSite("None")
//                .secure(true)
//                .httpOnly(true)
//                .maxAge(60 * 60 * 24)
//                .build();
//
//        return cookie;
//    }
//}