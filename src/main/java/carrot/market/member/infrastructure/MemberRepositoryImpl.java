package carrot.market.member.infrastructure;

import carrot.market.common.baseutil.BaseException;
import carrot.market.common.baseutil.BaseResponseStatus;
import carrot.market.member.domain.Member;
import carrot.market.member.entity.MemberEntity;
import carrot.market.member.service.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(MemberEntity.from(member)).to();
    }

    @Override
    public boolean existsByEmail(String email) {
        return memberJpaRepository.existsByEmail(email);
    }

    @Override
    public Member getById(long id) {
        return memberJpaRepository.findById(id).orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_EXISTED_USER)).to();
    }
}
