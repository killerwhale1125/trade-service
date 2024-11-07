package carrot.market.member.service.port;

import carrot.market.member.domain.Member;
import carrot.market.member.domain.MemberCreate;
import carrot.market.member.entity.MemberEntity;

public interface MemberRepository {
    Member save(Member member);

    boolean existsByEmail(String email);

    Member getById(long id);
}
