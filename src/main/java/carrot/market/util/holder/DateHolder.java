package carrot.market.util.holder;

import java.util.Date;

public interface DateHolder {

    Date createDate();

    Date createExpireDate(long now, long accessExpirationTime);
}
