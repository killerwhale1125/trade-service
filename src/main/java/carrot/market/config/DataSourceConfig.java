package carrot.market.config;

import carrot.market.util.RoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

import static carrot.market.util.DatabaseType.*;

/**
 * 읽기 전용 트랜잭션은 SLAVE DB 사용
 * 쓰기 전용 트랜잭션은 MASTER DB를 사용
 * @EnableTransactionManagement 는 @Transactional 어노테이션을 찾아 트랜잭션 범위를 활성화
 */
@Configuration  // -> 스프링부트 자동구성으로 인하여 생략 가능
@EnableTransactionManagement
@RequiredArgsConstructor
@ComponentScan(basePackages = {"carrot.market"})
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public DataSource routingDataSource() {
        RoutingDataSource routingDataSource = new RoutingDataSource();

        HashMap<Object, Object> dataSourceMap = new HashMap<>();
        /**
         * Master에는 Master DB
         * Slave에는 Slave DB
         */
        dataSourceMap.put(MASTER, masterDataSource());
        dataSourceMap.put(SLAVE, slaveDataSource());

        /**
         * default MASTER
         */
        routingDataSource.setDefaultTargetDataSource(masterDataSource());
        routingDataSource.setTargetDataSources(dataSourceMap);

        return routingDataSource;
    }

    /**
     * @Transactional은 실질적인 쿼리의 실행과 상관없이 트랜잭션이 실행되면 무조건 Connection 객체를 확보하는 방식으로
     * 동작합니다.
     * Replication을 적용하여 @Transaction(readOnly=true) 여부에 따라서 MASTER 또는 SLAVE 서버를 지정하여
     * 클라이언트의 요청을 수행하기 위해서는 JPA에서의 지연로딩 전략과 마찬가지로 트랜잭션 시작 시에는
     * 일단 Proxy Connection 객체를 획득한 다음 실제 쿼리가 나가는 시점에 특정 데이터베이스의 Connection을 획득하는
     * 방식으로 문제를 해결할 수 있는데 이를 LazyConnectionDataSourceProxy를 통해 설정할 수 있습니다.
     *
     * LazyConnectionDataSourceProxy를 이용한 트랜잭션 로직은 다음과 같이 변경됩니다.
     * TransactionManager 선택 -> LazyConnectionDataSourceProxy에서 Proxy Connection 획득 ->
     * 트랜잭션 동기화 -> 실제 쿼리 호출시에 실제 사용할 DataSource의 Connection 획득
     *
     * @DependsOn을 통해 빈이 등록되는 순서를 설정해주지 않으면 순환참조가 발생할 수 있습니다.
     * ┌─────┐
     * |  dataSource
     * ↑     ↓
     * |  routingDataSource
     * ↑     ↓
     * |  masterDataSource
     * ↑     ↓
     * |  org.springframework.boot.autoconfigure.jdbc.DataSourceInitializerInvoker
     * └─────┘
     */
    @Primary
    @Bean
    @DependsOn({"masterDataSource", "slaveDataSource", "routingDataSource"})
    public DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource());
    }

    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("masterDataSource") DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }

}
