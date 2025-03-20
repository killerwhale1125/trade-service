package carrot.market.member.dto.request;

import carrot.market.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class ProfileResponseDto {
    private final String email;
    private final String nickname;

    public static ProfileResponseDto of(Member member) {
        return ProfileResponseDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }
}
