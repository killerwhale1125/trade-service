package carrot.market.util.holder;

public interface PasswordEncoderHolder {
    String encode(String password);

    boolean isNotMatchPwd(String oldPassword, String password);
}
