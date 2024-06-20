package carrot.market.service;

import carrot.market.dto.request.member.LoginRequestDto;
import carrot.market.entity.member.Member;
import carrot.market.exception.MemberNotFoundException;
import carrot.market.repository.member.MemberRepository;
import carrot.market.util.jwt.JwtToken;
import carrot.market.util.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public boolean isDuplicatedEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Transactional
    public void registrationMember(Member member) {
        memberRepository.save(member);
    }

    public boolean isValidMember(LoginRequestDto loginRequestDto, PasswordEncoder passwordEncoder) {
        Member member = memberRepository.findMemberByEmail(loginRequestDto.getUsername()).orElseThrow(MemberNotFoundException::new);

        if(!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            return false;
        }

        return true;
    }

    @Transactional
    public JwtToken signIn(String username, String password) {
        /**
         * 1. username + password 를 기반으로 Authentication 객체 생성
         * 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
         */
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        /**
         * 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
         */
        AuthenticationManager authenticationManager = authenticationManagerBuilder.getObject();

        /**
         * Manager에게 인증되지 않은 authenticationToken 객체를 넘겨줘서 판별
         * authenticate 메서드가 실행될 때 MemberDetailsService 에서 만든 loadUserByUsername 메서드 실행
         * Manager는 Authentication 객체를 인증할 적절한 Provider를 찾아야 함
         * 따라서 loadUserByUsername을 호출하는 것은 AuthenticationProvider에 의해 호출됨
         */
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        /**
         * 3. 인증 정보를 기반으로 JWT 토큰 생성
         */
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }

}
