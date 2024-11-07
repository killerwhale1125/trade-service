package carrot.market.member.controller;

import carrot.market.common.baseutil.BaseResponse;
import carrot.market.member.controller.port.MemberService;
import carrot.market.member.controller.response.MemberResponse;
import carrot.market.member.domain.*;
import carrot.market.member.entity.MemberEntity;
import carrot.market.util.holder.PasswordEncoderHolder;
import carrot.market.util.jwt.JwtToken;
import carrot.market.util.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@Builder
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoderHolder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /** 회원 가입 **/
    @PostMapping
    public BaseResponse<MemberResponse> create(@RequestBody @Valid MemberCreate memberCreate) {
        memberService.isDuplicatedEmail(memberCreate.getEmail());
        return new BaseResponse<>(MemberResponse.from(memberService.create(memberCreate)));
    }

    /**
     * 이메일 중복 체크
     */
    @GetMapping("/duplicate/{email}")
    public BaseResponse<Void> isDuplicatedEmail(@PathVariable String email) {
        // 중복 체크
        memberService.isDuplicatedEmail(email);
        return new BaseResponse<>();
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public BaseResponse<JwtToken> login(@RequestBody @Valid MemberLogin memberLogin) {
        JwtToken jwtToken = memberService.signIn(memberLogin);
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
        MemberEntity memberEntity = memberService.findMemberByEmail(authentication.getName());
        return new BaseResponse<>(ProfileResponseDto.of(memberEntity));
    }

    /**
     * 프로필 정보 변경
     */
    @PutMapping("/my-profile")
    public BaseResponse<ProfileResponseDto> updateMemberProfile(Authentication authentication,
                                                               @RequestBody ProfileRequestDto profileRequest) {
        MemberEntity memberEntity = memberService.findMemberByEmail(authentication.getName());
        memberService.updateMemberProfile(memberEntity, profileRequest);

        return new BaseResponse<>(ProfileResponseDto.of(memberEntity));
    }

    /**
     * 패스워드 변경
     */
    @PutMapping("/password")
    public BaseResponse<Void> changePassword(Authentication authentication,
                                                     @RequestBody @Valid PasswordRequestDto passwordRequestDto) {
        MemberEntity memberEntity = memberService.findMemberByEmail(authentication.getName());
        if(memberService.isValidPassword(memberEntity, passwordRequestDto, passwordEncoder)) {
            memberService.updateMemberPassword(memberEntity, passwordRequestDto, passwordEncoder);
        }

        return new BaseResponse<>();
    }

    /**
     * 주소 변경
     */
    @PutMapping("/my-location")
    public BaseResponse<Void> setMemberLocationAddress(Authentication authentication,
                                                               @RequestBody LocationAddressRequestDto locationAddressRequest) {
        MemberEntity memberEntity = memberService.findMemberByEmail(authentication.getName());
        memberService.setMemberLocationAddress(memberEntity, locationAddressRequest);

        return new BaseResponse<>();
    }
}
