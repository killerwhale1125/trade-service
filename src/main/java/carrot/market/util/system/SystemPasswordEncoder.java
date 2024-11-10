package carrot.market.util.system;

import carrot.market.util.holder.PasswordEncoderHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SystemPasswordEncoder implements PasswordEncoderHolder {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean matches(String oldPassword, String password) {
        return passwordEncoder.matches(oldPassword, password);
    }
}
