package carrot.market.member.entity;

import carrot.market.member.domain.Member;
import carrot.market.member.domain.LocationAddressRequestDto;
import carrot.market.post.entity.Address;
import carrot.market.post.entity.Location;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String password;

    private String nickname;

    @Embedded
    private Address address;

    @Embedded
    private Location location;

    @Enumerated
    private MemberRole roles;

    @Builder
    public MemberEntity(String email, String password, String nickname, Address address) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
    }

    public static MemberEntity from(Member member) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.id = member.getId();
        memberEntity.password = member.getPassword();
        memberEntity.email = member.getEmail();
        memberEntity.nickname = member.getNickname();
        memberEntity.address = member.getAddress();
        memberEntity.location = member.getLocation();
        memberEntity.roles = member.getRoles();
        return memberEntity;
    }

    public void updateProfile(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateMemberLocationAddress(LocationAddressRequestDto locationAddressRequest) {
        this.address = locationAddressRequest.toAddress();
        this.location = locationAddressRequest.toLocation();
    }

    public Member to() {
        return Member.builder()
                .id(id)
                .password(password)
                .email(email)
                .nickname(nickname)
                .address(address)
                .location(location)
                .roles(roles)
                .build();
    }
}