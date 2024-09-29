package carrot.market.member.service;

import carrot.market.common.baseutil.BaseException;
import carrot.market.member.dto.LocationAddressRequestDto;
import carrot.market.member.dto.LoginRequestDto;
import carrot.market.member.dto.PasswordRequestDto;
import carrot.market.member.dto.ProfileRequestDto;
import carrot.market.member.entity.Member;
import carrot.market.member.repository.MemberRepository;
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

import static carrot.market.common.baseutil.BaseResponseStatus.*;

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
        Member member = memberRepository.findMemberByEmail(loginRequestDto.getUsername()).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));

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
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e){
            throw new BaseException(SIGN_IN_FAIL);
        }

        /**
         * 3. 인증 정보를 기반으로 JWT 토큰 생성
         */
        return jwtTokenProvider.generateToken(authentication);
    }

    public void updateMemberProfile(Member member, ProfileRequestDto profileRequest) {
        member.updateProfile(profileRequest.getNickname());
    }

    public boolean isValidPassword(Member member, PasswordRequestDto passwordRequestDto, PasswordEncoder passwordEncoder) {
        // old PW 검증
        if(passwordEncoder.matches(passwordRequestDto.getOldPassword(), member.getPassword())) {
            return true;
        } else {
            throw new BaseException(NOT_MATCHED_PASSWORD);
        }
    }

    public void updateMemberPassword(Member member, PasswordRequestDto passwordRequestDto, PasswordEncoder passwordEncoder) {
        member.updatePassword(passwordEncoder.encode(passwordRequestDto.getNewPassword()));
    }

    public void setMemberLocationAddress(Member member, LocationAddressRequestDto locationAddressRequest) {
        member.updateMemberLocationAddress(locationAddressRequest);
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));
    }
}
