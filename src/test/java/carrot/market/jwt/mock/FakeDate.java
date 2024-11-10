package carrot.market.jwt.mock;

import carrot.market.util.holder.DateHolder;
import lombok.Getter;

import java.util.Date;

@Getter
public class FakeDate implements DateHolder {

    private final Date date;
    private final Date expireDate;

    public FakeDate(Date date, Date expireDate) {
        this.date = date;
        this.expireDate = expireDate;
    }

    @Override
    public Date createDate() {
        return date;
    }

    @Override
    public Date createExpireDate(long now, long accessExpirationTime) {
        return expireDate;
    }
}
