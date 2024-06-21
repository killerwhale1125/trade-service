package carrot.market.member.repository;

import carrot.market.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQueryRepository {

    boolean existsByEmail(String email);

    Optional<Member> findMemberByEmail(String email);
}
