package carrot.market.member.repository;

import carrot.market.member.dto.response.MemberResponse;
import carrot.market.member.entity.Member;

public interface MemberRepository {
    MemberResponse save(Member member);

    boolean existsByEmail(String email);

    Member getById(long id);

    Member findByEmail(String email);
}
