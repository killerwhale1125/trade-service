package carrot.market.member.service;

import carrot.market.common.baseutil.BaseException;
import carrot.market.member.entity.MemberEntity;
import carrot.market.member.entity.MemberDetails;
import carrot.market.member.infrastructure.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static carrot.market.common.baseutil.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberJpaRepository memberJpaRepository;

    /**
     * Provider가 loadUserByUsername을 호출한다
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MemberEntity memberEntity = memberJpaRepository.findMemberByEmail(email).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));
        return new MemberDetails(memberEntity);
    }
}
