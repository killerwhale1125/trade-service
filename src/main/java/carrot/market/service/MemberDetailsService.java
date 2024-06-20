package carrot.market.service;

import carrot.market.entity.member.Member;
import carrot.market.exception.MemberNotFoundException;
import carrot.market.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        /**
         * 조회 성공 -> Authentication 객체에 들어갈 UserDetails 저장
         * 조회 실패 -> MemberNotFoundException 예외 처리
         */
        UserDetails userDetails = memberRepository.findMemberByEmail(email)
                .map(this::createUserDetails)
                .orElseThrow(() -> new MemberNotFoundException("해당하는 회원을 찾을 수 없습니다."));

        return userDetails;
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 return
    private UserDetails createUserDetails(Member member) {
        UserDetails build = User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .build();

        return build;
    }

}