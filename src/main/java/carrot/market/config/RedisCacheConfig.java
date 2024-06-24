package carrot.market.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.HashMap;
import java.util.Map;

import static carrot.market.config.CacheExpireConfig.*;
import static carrot.market.config.CacheKeyConfig.*;


@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Value("${spring.redis.cache.host}")
    private String host;

    @Value("${spring.redis.cache.port}")
    private int port;

    /**
     * Redis에 저장될 객체를 JSON으로 직렬화하여 변경할 때 사용할 ObjectMapper를 설정
     **/
    @Bean
    public ObjectMapper redisObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(BasicPolymorphicTypeValidator
                        .builder()
                        .allowIfSubType(Object.class)
                        .build()
                , ObjectMapper.DefaultTyping.EVERYTHING);
        objectMapper.registerModules(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);

        return objectMapper;
    }

    /**
     * 일반적인 JSON 직렬화
     */
    @Bean
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json()
                .serializationInclusion(JsonInclude.Include.NON_NULL)   // null 값이 포함된 속성은 직렬화에서 제외
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .modules(new JavaTimeModule())
                .build();
    }

    /**
     * Redis 서버 연결 설정 (Cache)
     */
    @Bean
    public RedisConnectionFactory redisCacheConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
    }

    /**
     * 1. Redis 캐시 관리자를 설정하고 초기화
     *   -> 레디스는 기본설정으로 인해 직렬/역직렬화를 직접 설정해야함
     * 2. 만료시간 설정
     * 3. 레디스 커넥션 생성
     */
    @Bean
    public RedisCacheManager redisCacheManager(
            @Qualifier("redisCacheConnectionFactory") RedisConnectionFactory redisConnectionFactory,
            @Qualifier("redisObjectMapper") ObjectMapper objectMapper) {

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues() // -> null 값을 캐시하지 않도록 설정
                .serializeValuesWith(RedisSerializationContext.SerializationPair    // -> 값을 직렬화하는 방식을 지정
                        .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));

        Map<String, RedisCacheConfiguration> configurations = new HashMap<>();
        // POST란 이름의 캐시를 만료기간을 포함하여 설정
        configurations.put(POST, redisCacheConfiguration.entryTtl(POST_CACHE_EXPIRE_TIME));
        // CATEGORY란 이름의 캐시를 만료기간을 포함하여 설정
        configurations.put(CATEGORY, redisCacheConfiguration.entryTtl(CATEGORY_CACHE_EXPIRE_TIME));

        return RedisCacheManager.builder(redisConnectionFactory)
                .withInitialCacheConfigurations(configurations) // -> 초기 캐시 구성을 설정. 여기서는 POST와 CATEGORY 캐시의 만료 시간을 설정
                .build();
    }


}
