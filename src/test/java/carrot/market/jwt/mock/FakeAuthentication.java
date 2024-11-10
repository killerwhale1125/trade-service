package carrot.market.jwt.mock;

import carrot.market.util.holder.AuthenticationHolder;

public class FakeAuthentication implements AuthenticationHolder {

    private final String name;

    public FakeAuthentication(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
