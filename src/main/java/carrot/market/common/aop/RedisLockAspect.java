package carrot.market.common.aop;

import carrot.market.common.baseutil.BaseException;
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

import static carrot.market.common.baseutil.BaseResponseStatus.REDIS_TRANSACTION_FAIL;

@Slf4j
@Aspect
@Component
public class RedisLockAspect {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Around("@annotation(carrot.market.common.annotation.RedisTransactional)")
    public Object aroundRedisTransaction(ProceedingJoinPoint joinPoint) {

        final Object[] result = {null};

        // Redis 트랜잭션 시작 (MULTI)
        List<Object> transactionResults = redisTemplate.execute(new SessionCallback<>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi(); // 트랜잭션 시작
                try {
                    result[0] = joinPoint.proceed();    // 실제 메서드 호출
                } catch (BaseException e) {
                    operations.discard(); // 특정 예외 발생 시 롤백하고 원래 예외를 던짐
                    throw e; // 원래의 예외를 던짐
                } catch (Throwable e) {
                    operations.discard(); // 트랜잭션 중단 (롤백)
                    throw new BaseException(REDIS_TRANSACTION_FAIL);
                }
                return operations.exec(); // 정상 응답 시 트랜잭션 커밋 (EXEC)
            }
        });

        // 트랜잭션 실패 (커밋 후의 로직 처리)
        if (transactionResults == null || transactionResults.isEmpty()) {
            throw new BaseException(REDIS_TRANSACTION_FAIL);
        }

        // 성공 시, 실제 메서드의 반환값 전달
        return result[0];
    }

}