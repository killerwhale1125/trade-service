package carrot.market.member.domain;

import carrot.market.member.entity.MemberRole;
import carrot.market.post.entity.Address;
import carrot.market.post.entity.Location;
import carrot.market.util.holder.PasswordEncoderHolder;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Member {
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private Address address;
    private Location location;
    private MemberRole roles;

    @Builder
    public Member(Long id, String email, String password, String nickname, Address address, Location location, MemberRole roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
        this.location = location;
        this.roles = roles;
    }

    public static Member from(MemberCreate memberCreate, PasswordEncoderHolder passwordEncoder) {
        return Member.builder()
                .email(memberCreate.getEmail())
                .password(passwordEncoder.encode(memberCreate.getPassword()))
                .nickname(memberCreate.getNickname())
                .address(new Address(memberCreate.getState(), memberCreate.getCity(), memberCreate.getTown()))
                .build();
    }
}
