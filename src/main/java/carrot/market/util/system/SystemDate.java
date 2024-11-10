package carrot.market.util.system;

import carrot.market.util.holder.DateHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SystemDate implements DateHolder {

    @Override
    public Date createDate() {
        return new Date();
    }

    @Override
    public Date createExpireDate(long now, long accessExpirationTime) {
        return new Date(now + accessExpirationTime);
    }
}
