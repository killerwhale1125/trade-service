package carrot.market.member.service;

import carrot.market.common.annotation.RedisTransactional;
import carrot.market.common.baseutil.BaseException;
import carrot.market.member.dto.request.*;
import carrot.market.member.dto.response.MemberResponse;
import carrot.market.member.entity.Member;
import carrot.market.member.repository.MemberJpaRepository;
import carrot.market.member.repository.MemberRepository;
import carrot.market.util.holder.AuthenticationHolder;
import carrot.market.util.holder.PasswordEncoderHolder;
import carrot.market.util.jwt.JwtToken;
import carrot.market.util.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
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
public class MemberServiceImpl implements MemberService {
    private final MemberJpaRepository memberJpaRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoderHolder passwordEncoder;
    private final AuthenticationHolder authenticationHolder;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional
    public MemberResponse create(MemberCreate memberCreate) {
        if (isDuplicatedEmail(memberCreate.getEmail())) {
            throw new BaseException(DUPLICATE_EMAIL);
        }

        Member member = Member.create(memberCreate, passwordEncoder);

        return memberRepository.save(member);
    }

    @Override
    public boolean isDuplicatedEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean isValidMember(MemberLogin memberLogin, PasswordEncoder passwordEncoder) {
        Member member = memberJpaRepository.findByEmail(memberLogin.getEmail()).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));

        if(!passwordEncoder.matches(memberLogin.getPassword(), member.getPassword())) {
            return false;
        }

        return true;
    }

    @Override
    @Transactional
    public JwtToken login(MemberLogin memberLogin) {
        Member findMember = memberRepository.findByEmail(memberLogin.getEmail());

        if(passwordEncoder.isNotMatchPwd(memberLogin.getPassword(), findMember.getPassword())) {
            throw new BaseException(NOT_MATCHED_PASSWORD);
        }

        return jwtTokenProvider.generateToken(findMember.getEmail());
    }

    public void updateMemberProfile(Member member, ProfileRequestDto profileRequest) {
        member.updateProfile(profileRequest.getNickname());
    }

    @Override
    public boolean isValidPassword(Member member, PasswordDto passwordDto, PasswordEncoderHolder passwordEncoder) {
        // old PW 검증
        if(passwordEncoder.isNotMatchPwd(passwordDto.getOldPassword(), member.getPassword())) {
            return true;
        } else {
            throw new BaseException(NOT_MATCHED_PASSWORD);
        }
    }

    @Override
    public void updateMemberPassword(Member member, PasswordDto passwordDto, PasswordEncoderHolder passwordEncoder) {

    }

    public boolean isValidPassword(Member member, PasswordDto passwordDto, PasswordEncoder passwordEncoder) {
        // old PW 검증
        if(passwordEncoder.matches(passwordDto.getOldPassword(), member.getPassword())) {
            return true;
        } else {
            throw new BaseException(NOT_MATCHED_PASSWORD);
        }
    }

    public void updateMemberPassword(Member member, PasswordDto passwordDto, PasswordEncoder passwordEncoder) {
        member.updatePassword(passwordEncoder.encode(passwordDto.getNewPassword()));
    }

    public void setMemberLocationAddress(Member member, LocationAddressDto locationAddressRequest) {
        member.updateMemberLocationAddress(locationAddressRequest);
    }

    @Override
    @RedisTransactional
    public void redisTest() {
        String key = "key";
        ListOperations<String, String> nameList = redisTemplate.opsForList();
        for (int i = 1; i <= 10; i++) {
            nameList.rightPush(key, "홍길동" + i);
        }
        /* 이 때 예시로 사용자를 찾을 수 없다는 예외가 발생*/
        throw new BaseException(NOT_EXISTED_USER);
    }

    public Member findMemberByEmail(String email) {
        return memberJpaRepository.findByEmail(email).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));
    }

}
