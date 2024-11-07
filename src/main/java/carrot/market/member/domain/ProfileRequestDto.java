package carrot.market.member.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class ProfileRequestDto {
    private final String email;
    private final String nickname;
}
