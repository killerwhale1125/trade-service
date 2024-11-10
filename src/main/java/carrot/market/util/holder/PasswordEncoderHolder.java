package carrot.market.util.holder;

public interface PasswordEncoderHolder {
    String encode(String password);

    boolean matches(String oldPassword, String password);
}
