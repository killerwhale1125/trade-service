package carrot.market.member.infrastructure;

import carrot.market.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long>, MemberQueryRepository {

    boolean existsByEmail(String email);

    Optional<MemberEntity> findMemberByEmail(String email);
}
