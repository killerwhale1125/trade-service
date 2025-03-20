package carrot.market.common.aop;

import carrot.market.common.baseutil.BaseException;
import carrot.market.member.entity.Member;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static carrot.market.common.baseutil.BaseResponseStatus.NOT_EXISTED_AREA;
import static carrot.market.common.baseutil.BaseResponseStatus.NOT_EXISTED_USER;

/**
 * 회원의 지역 정보가 유효한지 AOP 검증
 */
@Aspect
@Component
public class AreaInfoAspect {

    /**
     * @AreaInfoRequired 가 붙은 메소드가 실행되기 전 호출
     * isValidAreaInfo 는 @Before advice로 지정됨
     * jointPoint -> 호출됬던 메소드 매개변수
     * Arrays.stream(joinPoint.getArgs()) -> 매개변수를 인자로 가져와 Stream으로 변환
     * filter(obj1 -> Member.class.isInstance(obj1)) -> 매개변수 인자 중 Member클래스만 필터링하여 가져옴
     * map(obj -> Member.class.cast(obj)) -> 필터링하여 가져온 객체를 Member 클래스로 Cast 형변환
     * findFirst() -> 첫번째 Member 객체 조회
     * orElseThrow(MemberNotFoundException::new); -> 없으면 예외 처리
     * **/
    @Before("@annotation(carrot.market.common.annotation.AreaInfoRequired)")
    public void isValidAreaInfo(JoinPoint joinPoint) {
        Member member = Arrays.stream(joinPoint.getArgs())
                .filter(obj1 -> Member.class.isInstance(obj1))
                .map(obj -> Member.class.cast(obj))
                .findFirst()
                .orElseThrow(() -> new BaseException(NOT_EXISTED_USER));


//        if(member.getAddress() == null || member.getLocation() == null) {
//            throw new BaseException(NOT_EXISTED_AREA);
//        }
        if(member.getAddress() == null) {
            throw new BaseException(NOT_EXISTED_AREA);
        }
    }
}
