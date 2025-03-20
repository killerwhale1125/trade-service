package carrot.market.member.dto.response;

import carrot.market.member.entity.MemberRole;
import carrot.market.post.entity.Address;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {
    private Long id;

    private String email;

    private String nickname;

    private MemberRole roles;

    private Address address;

    @Builder
    public MemberResponse(Long id, String email, String password, String nickname, MemberRole roles, Address address) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.roles = roles;
        this.address = address;
    }

}
