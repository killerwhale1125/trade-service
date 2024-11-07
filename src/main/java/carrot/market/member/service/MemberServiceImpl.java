package carrot.market.member.service;

import carrot.market.common.baseutil.BaseException;
import carrot.market.member.controller.port.MemberService;
import carrot.market.member.domain.*;
import carrot.market.member.entity.MemberEntity;
import carrot.market.member.infrastructure.MemberJpaRepository;
import carrot.market.member.service.port.MemberRepository;
import carrot.market.util.holder.AuthenticationHolder;
import carrot.market.util.holder.PasswordEncoderHolder;
import carrot.market.util.jwt.JwtToken;
import carrot.market.util.jwt.JwtTokenProvider;
import carrot.market.util.system.SystemAuthentication;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static carrot.market.common.baseutil.BaseResponseStatus.*;

@Builder
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberJpaRepository memberJpaRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoderHolder passwordEncoder;
    private final AuthenticationHolder authenticationHolder;

    @Override
    @Transactional
    public Member create(MemberCreate memberCreate) {
       return memberRepository.save(Member.from(memberCreate, passwordEncoder));
    }

    @Override
    public void isDuplicatedEmail(String email) {
        if(!memberRepository.existsByEmail(email)) {
            throw new BaseException(DUPLICATE_NICKNAME);
        }
    }

    public boolean isValidMember(MemberLogin memberLogin, PasswordEncoder passwordEncoder) {
        MemberEntity memberEntity = memberJpaRepository.findMemberByEmail(memberLogin.getUsername()).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));

        if(!passwordEncoder.matches(memberLogin.getPassword(), memberEntity.getPassword())) {
            return false;
        }

        return true;
    }

    @Override
    @Transactional
    public JwtToken signIn(MemberLogin memberLogin) {
        /**
         * 1. username + password 를 기반으로 Authentication 객체 생성
         * 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
         */
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(memberLogin.getUsername(), memberLogin.getPassword());

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
        return jwtTokenProvider.generateToken(authenticationHolder);
    }

    public void updateMemberProfile(MemberEntity memberEntity, ProfileRequestDto profileRequest) {
        memberEntity.updateProfile(profileRequest.getNickname());
    }

    @Override
    public boolean isValidPassword(MemberEntity memberEntity, PasswordRequestDto passwordRequestDto, PasswordEncoderHolder passwordEncoder) {
        // old PW 검증
        if(passwordEncoder.matches(passwordRequestDto.getOldPassword(), memberEntity.getPassword())) {
            return true;
        } else {
            throw new BaseException(NOT_MATCHED_PASSWORD);
        }
    }

    @Override
    public void updateMemberPassword(MemberEntity memberEntity, PasswordRequestDto passwordRequestDto, PasswordEncoderHolder passwordEncoder) {

    }

    public boolean isValidPassword(MemberEntity memberEntity, PasswordRequestDto passwordRequestDto, PasswordEncoder passwordEncoder) {
        // old PW 검증
        if(passwordEncoder.matches(passwordRequestDto.getOldPassword(), memberEntity.getPassword())) {
            return true;
        } else {
            throw new BaseException(NOT_MATCHED_PASSWORD);
        }
    }

    public void updateMemberPassword(MemberEntity memberEntity, PasswordRequestDto passwordRequestDto, PasswordEncoder passwordEncoder) {
        memberEntity.updatePassword(passwordEncoder.encode(passwordRequestDto.getNewPassword()));
    }

    public void setMemberLocationAddress(MemberEntity memberEntity, LocationAddressRequestDto locationAddressRequest) {
        memberEntity.updateMemberLocationAddress(locationAddressRequest);
    }

    public MemberEntity findMemberByEmail(String email) {
        return memberJpaRepository.findMemberByEmail(email).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));
    }

}
