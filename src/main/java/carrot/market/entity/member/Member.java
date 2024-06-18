package carrot.market.entity.member;

import carrot.market.common.BaseTimeEntity;
import carrot.market.entity.post.Address;
import carrot.market.entity.post.Location;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    private String email;

    private String password;

    private String nickname;

    @Embedded
    private Address address;

    @Embedded
    private Location location;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Builder
    public Member(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
