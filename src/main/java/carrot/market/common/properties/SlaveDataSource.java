package carrot.market.common.properties;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.datasource.slave")
@AllArgsConstructor
public class SlaveDataSource {
    private final String url;
    private final String username;
    private final String password;
    private final String driverClassName;

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }
}
