package carrot.market.member.service;

import carrot.market.member.dto.request.*;
import carrot.market.member.dto.response.MemberResponse;
import carrot.market.member.entity.Member;
import carrot.market.util.holder.PasswordEncoderHolder;
import carrot.market.util.jwt.JwtToken;

public interface MemberService {
    MemberResponse create(MemberCreate memberCreate);

    boolean isDuplicatedEmail(String email);

    JwtToken login(MemberLogin memberLogin);

    Member findMemberByEmail(String name);

    void updateMemberProfile(Member member, ProfileRequestDto profileRequest);

    boolean isValidPassword(Member member, PasswordDto passwordDto, PasswordEncoderHolder passwordEncoder);

    void updateMemberPassword(Member member, PasswordDto passwordDto, PasswordEncoderHolder passwordEncoder);

    void setMemberLocationAddress(Member member, LocationAddressDto locationAddressRequest);

    void redisTest();

}
