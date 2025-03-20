package carrot.market.member.service;

import carrot.market.common.baseutil.BaseException;
import carrot.market.member.entity.Member;
import carrot.market.member.entity.MemberDetails;
import carrot.market.member.repository.MemberJpaRepository;
import carrot.market.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static carrot.market.common.baseutil.BaseResponseStatus.NOT_EXISTED_USER;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * Provider가 loadUserByUsername을 호출한다
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new MemberDetails(memberRepository.findByEmail(email));
    }
}
