package carrot.market.member.entity;

import carrot.market.member.dto.LocationAddressRequestDto;
import carrot.market.post.entity.Address;
import carrot.market.post.entity.Location;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

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
}