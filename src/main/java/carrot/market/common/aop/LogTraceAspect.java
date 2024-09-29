package carrot.market.common.aop;

import carrot.market.common.trace.LogTrace;
import carrot.market.common.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class LogTraceAspect {

    private final LogTrace trace;

    public LogTraceAspect(LogTrace logTrace) {
        this.trace = logTrace;
    }

    /**
     * Advisor
     */
    @Around("carrot.market.common.pointcut.CommonPointcut.commonPointcut()") // -> Pointcut
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable { // -> Advice
        TraceStatus status = null;
        try {
            String message = joinPoint.getSignature().toShortString();
            status = trace.begin(message);

            Object result = joinPoint.proceed();
            trace.end(status);
            return result;
        } catch(Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}

