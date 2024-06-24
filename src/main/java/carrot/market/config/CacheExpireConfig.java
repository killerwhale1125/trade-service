package carrot.market.config;

import java.time.Duration;

/**
 * 캐시 만료시간 설정
 */
public class CacheExpireConfig {

    // 기본 캐시 만료 10분
    public static final Duration DEFAULT_CACHE_EXPIRE_TIME = Duration.ofMinutes(10L);

    // 게시물 캐시 만료 5분
    public static final Duration POST_CACHE_EXPIRE_TIME = Duration.ofMinutes(5L);

    //카테고리 캐시 만료 1일
    public static final Duration CATEGORY_CACHE_EXPIRE_TIME = Duration.ofDays(1L);
}
