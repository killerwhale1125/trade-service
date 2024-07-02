package carrot.market.util;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static carrot.market.util.DatabaseType.*;


public class RoutingDataSource extends AbstractRoutingDataSource {

    // read-only true? -> SLAVE
    // read-only false? -> MASTER

    /**
     * 해당 메서드를 통하여 현재 트랜잭션이 읽기 전용인지 쓰기 전용인지 판단
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? SLAVE : MASTER;
    }
}
