package carrot.market.member.controller;

import carrot.market.common.baseutil.BaseResponse;
import carrot.market.member.dto.*;
import carrot.market.member.entity.Member;
import carrot.market.member.service.MemberService;
import carrot.market.util.jwt.JwtToken;
import carrot.market.util.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static carrot.market.common.HttpStatusResponseEntity.RESPONSE_OK;
import static carrot.market.common.baseutil.BaseResponseStatus.DUPLICATE_EMAIL;
import static carrot.market.common.baseutil.BaseResponseStatus.DUPLICATE_NICKNAME;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /** 회원 가입 **/
    @PostMapping
    public BaseResponse<Object> registration(@RequestBody @Valid MemberDto memberDto) {
        // 중복 체크
        boolean isDuplicated = memberService.isDuplicatedEmail(memberDto.getEmail());
        
        // 아이디 중복 o
        if(isDuplicated)
            return new BaseResponse<>(DUPLICATE_EMAIL);

        // 아이디 중복 X
        Member member = MemberDto.toEntity(memberDto, passwordEncoder);
        
        // 회원 등록
        memberService.registrationMember(member);
        return new BaseResponse<>();
    }

    /**
     * 이메일 중복 체크
     */
    @GetMapping("/duplicate/{email}")
    public BaseResponse<Object> isDuplicatedEmail(@PathVariable String email) {
        // 중복 체크
        boolean isDuplicated = memberService.isDuplicatedEmail(email);

        // 아이디 중복 o
        if(isDuplicated)
            return new BaseResponse<>(DUPLICATE_NICKNAME);

        return new BaseResponse<>();
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public BaseResponse<JwtToken> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();
        /**
         * 첫 로그인 시 JWT토큰 없이 요청하기 때문에 JWT 토큰을 발급함
         */
        JwtToken jwtToken = memberService.signIn(username, password);
        log.debug("request username = {}, password = {}", username, password);
        log.debug("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return new BaseResponse<>(jwtToken);
    }

    /**
     * Access 토큰 만료 시 Refresh 토큰으로 재발급 요청
     */
    @PostMapping("/refresh")
    public BaseResponse<JwtToken> refresh(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.resolveToken(request);
        return new BaseResponse<>(jwtTokenProvider.refreshTokens(refreshToken));
    }

    /**
     * 프로필 정보 조회
     */
    @GetMapping("/my-profile")
    public BaseResponse<ProfileResponseDto> getMemberProfile(Authentication authentication) {
        Member member = memberService.findMemberByEmail(authentication.getName());
        return new BaseResponse<>(ProfileResponseDto.of(member));
    }

    /**
     * 프로필 정보 변경
     */
    @PutMapping("/my-profile")
    public BaseResponse<ProfileResponseDto> updateMemberProfile(Authentication authentication,
                                                               @RequestBody ProfileRequestDto profileRequest) {
        Member member = memberService.findMemberByEmail(authentication.getName());
        memberService.updateMemberProfile(member, profileRequest);

        return new BaseResponse<>(ProfileResponseDto.of(member));
    }

    /**
     * 패스워드 변경
     */
    @PutMapping("/password")
    public BaseResponse<Void> changePassword(Authentication authentication,
                                                     @RequestBody @Valid PasswordRequestDto passwordRequestDto) {
        Member member = memberService.findMemberByEmail(authentication.getName());
        if(memberService.isValidPassword(member, passwordRequestDto, passwordEncoder)) {
            memberService.updateMemberPassword(member, passwordRequestDto, passwordEncoder);
        }

        return new BaseResponse<>();
    }

    /**
     * 주소 변경
     */
    @PutMapping("/my-location")
    public BaseResponse<Void> setMemberLocationAddress(Authentication authentication,
                                                               @RequestBody LocationAddressRequestDto locationAddressRequest) {
        Member member = memberService.findMemberByEmail(authentication.getName());
        memberService.setMemberLocationAddress(member, locationAddressRequest);

        return new BaseResponse<>();
    }
}
