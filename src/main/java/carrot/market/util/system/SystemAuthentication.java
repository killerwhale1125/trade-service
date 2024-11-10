package carrot.market.util.system;

import carrot.market.util.holder.AuthenticationHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SystemAuthentication implements AuthenticationHolder {

    @Override
    public String getName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
