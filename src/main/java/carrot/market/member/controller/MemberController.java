package carrot.market.member.controller;

import carrot.market.common.baseutil.BaseResponse;
import carrot.market.member.dto.request.*;
import carrot.market.member.dto.response.MemberResponse;
import carrot.market.member.entity.Member;
import carrot.market.member.service.MemberService;
import carrot.market.util.holder.PasswordEncoderHolder;
import carrot.market.util.jwt.JwtToken;
import carrot.market.util.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoderHolder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /* 회원 가입 */
    @PostMapping
    public BaseResponse<MemberResponse> create(@RequestBody @Valid MemberCreate memberCreate) {
        return new BaseResponse<>(memberService.create(memberCreate));
    }

    /* 이메일 중복 체크 */
    @GetMapping("/duplicate/{email}")
    public BaseResponse<Boolean> isDuplicatedEmail(@PathVariable String email) {
        return new BaseResponse<>(memberService.isDuplicatedEmail(email));
    }

    /* 로그인 */
    @PostMapping("/login")
    public BaseResponse<JwtToken> login(@RequestBody @Valid MemberLogin memberLogin, HttpServletResponse response) {
        MemberResponse memberResponse = memberService.login(memberLogin);
        return new BaseResponse<>(jwtTokenProvider.generateToken(memberResponse.getEmail(), memberResponse.getId(), response));
    }

    /**
     * Access 토큰 만료 시 Refresh 토큰으로 재발급 요청
     */
    @PostMapping("/refresh")
    public BaseResponse<JwtToken> refresh(HttpServletRequest request, HttpServletResponse response) {
        return new BaseResponse<>(jwtTokenProvider.reissueRefreshToken(request, response));
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
                                                     @RequestBody @Valid PasswordDto passwordDto) {
        Member member = memberService.findMemberByEmail(authentication.getName());
        if(memberService.isValidPassword(member, passwordDto, passwordEncoder)) {
            memberService.updateMemberPassword(member, passwordDto, passwordEncoder);
        }

        return new BaseResponse<>();
    }

    /**
     * 주소 변경
     */
    @PutMapping("/my-location")
    public BaseResponse<Void> setMemberLocationAddress(Authentication authentication,
                                                               @RequestBody LocationAddressDto locationAddressRequest) {
        Member member = memberService.findMemberByEmail(authentication.getName());
        memberService.setMemberLocationAddress(member, locationAddressRequest);

        return new BaseResponse<>();
    }
}
