package carrot.market.member.controller.response;

import carrot.market.member.domain.Member;
import carrot.market.member.entity.MemberRole;
import carrot.market.post.entity.Address;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {
    private Long id;

    private String email;

    private String password;

    private String nickname;

    private MemberRole roles;

    private Address address;

    @Builder
    public MemberResponse(Long id, String email, String password, String nickname, MemberRole roles, Address address) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.roles = roles;
        this.address = address;
    }

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .address(member.getAddress())
                .build();
    }
}
