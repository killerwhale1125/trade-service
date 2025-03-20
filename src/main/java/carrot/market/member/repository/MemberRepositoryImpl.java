package carrot.market.member.repository;

import carrot.market.common.baseutil.BaseException;
import carrot.market.member.dto.response.MemberResponse;
import carrot.market.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static carrot.market.common.baseutil.BaseResponseStatus.*;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public MemberResponse save(Member member) {
        return memberJpaRepository.save(member).toEntity();
    }

    @Override
    public boolean existsByEmail(String email) {
        return memberJpaRepository.existsByEmail(email);
    }

    @Override
    public Member getById(long id) {
        return memberJpaRepository.findById(id).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));
    }

    @Override
    public Member findByEmail(String email) {
        return memberJpaRepository.findByEmail(email).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));
    }
}
