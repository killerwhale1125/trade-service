package carrot.market.repository.member;

import carrot.market.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQueryRepository {

    boolean existsByEmail(String email);

    Optional<Member> findMemberByEmail(String email);
}
