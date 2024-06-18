package carrot.market.service;

import carrot.market.entity.member.Member;
import carrot.market.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public boolean isDuplicatedEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Transactional
    public void registrationMember(Member member) {
        memberRepository.save(member);
    }
}
