package carrot.market.controller;

import carrot.market.dto.request.member.LoginRequestDto;
import carrot.market.dto.request.member.MemberDto;
import carrot.market.entity.member.Member;
import carrot.market.service.LoginService;
import carrot.market.service.MemberService;
import carrot.market.util.jwt.JwtToken;
import carrot.market.util.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static carrot.market.common.HttpStatusResponseEntity.*;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;

    /** 회원 가입 **/
    @PostMapping
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid MemberDto memberDto) {
        // 중복 체크
        boolean isDuplicated = memberService.isDuplicatedEmail(memberDto.getEmail());
        
        // 아이디 중복 o
        if(isDuplicated)
            return RESPONSE_CONFLICT;

        // 아이디 중복 X
        Member member = MemberDto.toEntity(memberDto, passwordEncoder);
        
        // 회원 등록
        memberService.registrationMember(member);
        return RESPONSE_OK;
    }

    /**
     * 이메일 중복 체크
     */
    @GetMapping("/duplicate/{email}")
    public ResponseEntity<HttpStatus> isDuplicatedEmail(@PathVariable String email) {
        // 중복 체크
        boolean isDuplicated = memberService.isDuplicatedEmail(email);

        // 아이디 중복 o
        if(isDuplicated)
            return RESPONSE_CONFLICT;

        return RESPONSE_OK;
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public JwtToken login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();
        /**
         * 첫 로그인 시 JWT토큰 없이 요청하기 때문에 JWT 토큰을 발급함
         */
        JwtToken jwtToken = memberService.signIn(username, password);
        log.debug("request username = {}, password = {}", username, password);
        log.debug("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }

    /**
     * Access 토큰 만료 시 Refresh 토큰으로 재발급 요청
     */
    @PostMapping("/refresh")
    public JwtToken refresh(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.resolveToken(request);
        return jwtTokenProvider.refreshTokens(refreshToken);
    }

}
