package carrot.market.member.mock;

import carrot.market.util.holder.PasswordEncoderHolder;

public class TestPasswordEncoder implements PasswordEncoderHolder {

    private final String password;

    public TestPasswordEncoder(String password) {
        this.password = password;
    }

    @Override
    public String encode(String password) {
        return password;
    }

    @Override
    public boolean matches(String oldPassword, String password) {
        return false;
    }
}
