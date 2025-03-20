package carrot.market.member.dto.request;

import carrot.market.member.entity.Member;
import lombok.Builder;

@Builder
public record ProfileResponseDto(String email, String nickname) {
    public static ProfileResponseDto of(Member member) {
        return ProfileResponseDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }
}
