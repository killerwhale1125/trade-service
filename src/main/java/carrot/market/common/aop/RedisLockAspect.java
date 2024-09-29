package carrot.market.common.aop;

import carrot.market.common.baseutil.BaseException;
import carrot.market.common.baseutil.BaseResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static carrot.market.common.baseutil.BaseResponseStatus.*;

@Slf4j
@Aspect
@Component
public class RedisLockAspect {

    /**
     * RedisTemplate은 Thread Safe
     */
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String LOCK_PREFIX = "LOCK_";
    private static final long LOCK_EXPIRE_TIME = 1000; // 5초 동안 유효한 락

    @Around("@annotation(carrot.market.common.annotation.RedisTransactional)")
    public Object aroundRedisTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        String key = getKeyFromArgs(joinPoint.getArgs());  // 유저별로 unique key 생성
        String lockKey = LOCK_PREFIX + key;

        try {
            // 분산 락 시도
            boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "LOCK", LOCK_EXPIRE_TIME, TimeUnit.MILLISECONDS);
            if (!lockAcquired) {
                log.info("락 걸림");
                throw new BaseException(REDIS_LOCKED);
            }

            log.info("락 획득");
            final Object[] result = {null};

            // Redis 트랜잭션 시작 (MULTI)
            List<Object> transactionResults = redisTemplate.execute(new SessionCallback<List<Object>>() {
                @Override
                public List<Object> execute(RedisOperations operations) throws DataAccessException {
                    operations.multi(); // 트랜잭션 시작

                    try {
                        // 실제 메서드 호출 (ex: 토큰 생성 로직 수행)
                        result[0] = joinPoint.proceed();
                    } catch (BaseException e) {
                        // 특정 예외 발생 시 롤백하고 원래 예외를 던짐
                        operations.discard(); // 트랜잭션 중단 (롤백)
                        throw e; // 원래의 예외를 던짐
                    } catch (Throwable e) {
                        operations.discard(); // 트랜잭션 중단 (롤백)
                        throw new BaseException(DATABASE_ERROR); // 데이터베이스 오류
                    }

                    return operations.exec(); // 트랜잭션 커밋 (EXEC)
                }
            });

            // 트랜잭션 성공 (커밋 후의 로직 처리)
            if (transactionResults == null || transactionResults.isEmpty()) {
                throw new IllegalStateException("Redis transaction failed or aborted.");
            }

            // 성공 시, 실제 메서드의 반환값 전달
            return result[0];

        } catch (Exception e) {
            // 트랜잭션 실패 시 처리
            throw e;
        } finally {
            // 분산 락 해제
            redisTemplate.delete(lockKey);
        }
    }

    private String getKeyFromArgs(Object[] args) {
        // 예시로 첫 번째 파라미터를 키로 사용 (userId 또는 authentication.getName() 같은 값)
        return args[0].toString();
    }
}