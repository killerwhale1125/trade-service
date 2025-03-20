package carrot.market.member.entity;

import carrot.market.member.dto.request.LocationAddressDto;
import carrot.market.member.dto.request.MemberCreate;
import carrot.market.member.dto.request.MemberLogin;
import carrot.market.member.dto.response.MemberResponse;
import carrot.market.post.entity.Address;
import carrot.market.post.entity.Location;
import carrot.market.util.holder.PasswordEncoderHolder;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
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
    public Member(String email, String password, String nickname, Address address) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
    }

    public static Member create(MemberCreate memberCreate, PasswordEncoderHolder passwordEncoder) {
        Member member = new Member();
        member.email = memberCreate.getEmail();
        member.password = passwordEncoder.encode(memberCreate.getPassword());
        member.nickname = memberCreate.getNickname();

        Address address = Address.create(memberCreate.getState(), memberCreate.getCity(), memberCreate.getTown());
        member.address = address;

        return member;
    }

//    public static MemberEntity from(Member member) {
//        MemberEntity memberEntity = new MemberEntity();
//        memberEntity.id = member.getId();
//        memberEntity.password = member.getPassword();
//        memberEntity.email = member.getEmail();
//        memberEntity.nickname = member.getNickname();
//        memberEntity.address = member.getAddress();
//        memberEntity.location = member.getLocation();
//        memberEntity.roles = member.getRoles();
//        return memberEntity;
//    }

    public void updateProfile(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateMemberLocationAddress(LocationAddressDto locationAddressRequest) {
        this.address = locationAddressRequest.toAddress();
        this.location = locationAddressRequest.toLocation();
    }

    public MemberResponse toEntity() {
        return MemberResponse.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .address(address)
                .build();
    }

    public Member isNotMatchPwd(MemberLogin memberLogin, PasswordEncoderHolder passwordEncoderHolder) {
        if (passwordEncoderHolder.isNotMatchPwd(memberLogin.getPassword(), password)) {

        }
        return this;
    }
}