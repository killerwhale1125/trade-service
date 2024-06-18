package carrot.market.common.security.provider.controller;

import carrot.market.common.security.auth.PrincipalDetails;
import carrot.market.dto.member.MemberDto;
import carrot.market.entity.member.Member;
import carrot.market.service.LoginService;
import carrot.market.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static carrot.market.common.HttpStatusResponseEntity.RESPONSE_CONFLICT;
import static carrot.market.common.HttpStatusResponseEntity.RESPONSE_OK;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final LoginService loginService;

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
    public ResponseEntity<HttpStatus> login(@AuthenticationPrincipal PrincipalDetails userDetails) {
        return null;
    }
}
