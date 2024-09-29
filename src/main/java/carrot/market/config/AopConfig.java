package carrot.market.config;

import carrot.market.common.aop.LogTraceAspect;
import carrot.market.common.trace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AopConfig {

    // 해당 Bean이 어드바이저로 반환된다
    @Bean
    public LogTraceAspect logTraceAspect(LogTrace logTrace) {
        return new LogTraceAspect(logTrace);
    }
}
