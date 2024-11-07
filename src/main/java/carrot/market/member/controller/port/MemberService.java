package carrot.market.member.controller.port;

import carrot.market.member.domain.*;
import carrot.market.member.entity.MemberEntity;
import carrot.market.util.holder.PasswordEncoderHolder;
import carrot.market.util.jwt.JwtToken;

public interface MemberService {
    Member create(MemberCreate memberCreate);

    void isDuplicatedEmail(String email);

    JwtToken signIn(MemberLogin memberLogin);

    MemberEntity findMemberByEmail(String name);

    void updateMemberProfile(MemberEntity memberEntity, ProfileRequestDto profileRequest);

    boolean isValidPassword(MemberEntity memberEntity, PasswordRequestDto passwordRequestDto, PasswordEncoderHolder passwordEncoder);

    void updateMemberPassword(MemberEntity memberEntity, PasswordRequestDto passwordRequestDto, PasswordEncoderHolder passwordEncoder);

    void setMemberLocationAddress(MemberEntity memberEntity, LocationAddressRequestDto locationAddressRequest);
}
