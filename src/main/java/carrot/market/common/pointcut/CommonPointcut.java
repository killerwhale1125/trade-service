package carrot.market.common.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class CommonPointcut {

    @Pointcut("execution(* carrot.market..*Service.*(..))")
    public void servicePointcut() {}

    @Pointcut("execution(* carrot.market..*Controller.*(..))")
    public void controllerPointcut() {}

    @Pointcut("execution(* carrot.market..*Repository.*(..))")
    public void repositoryPointcut() {}

    @Pointcut("servicePointcut() || controllerPointcut() || repositoryPointcut()")
    public void commonPointcut() {}
}
