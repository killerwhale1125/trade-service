package carrot.market.member.domain;

import carrot.market.member.entity.MemberEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class ProfileResponseDto {
    private final String email;
    private final String nickname;

    public static ProfileResponseDto of(MemberEntity memberEntity) {
        return ProfileResponseDto.builder()
                .email(memberEntity.getEmail())
                .nickname(memberEntity.getNickname())
                .build();
    }
}
