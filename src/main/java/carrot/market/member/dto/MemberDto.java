package carrot.market.member.dto;

import carrot.market.member.entity.Member;
import carrot.market.post.entity.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberDto {
    @NotEmpty
    @Email(message = "유효하지 않은 이메일 형식입니다.",
            regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    private String email;

    @NotEmpty
    @Pattern(message = "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호를 입력해야 합니다.",
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!~$%^&-+=()])(?=\\S+$).{8,16}$")
    private String password;

    @NotEmpty
    private String nickname;

    @NotEmpty
    private String state;
    @NotEmpty
    private String city;
    @NotEmpty
    private String town;

    public static Member toEntity(MemberDto memberDto, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(memberDto.getEmail())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .nickname(memberDto.getNickname())
                .address(new Address(memberDto.getState(), memberDto.getCity(), memberDto.getTown()))
                .build();
    }
}
