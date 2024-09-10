package carrot.market.common.advice;

import carrot.market.common.baseutil.BaseException;
import carrot.market.common.baseutil.BaseResponse;
import carrot.market.common.baseutil.BaseResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice   // 글로벌 컨트롤러 적용
public class ExceptionControllerAdvice {

    @ExceptionHandler
    public BaseResponse<BaseResponseStatus> baseException(BaseException e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder logMessage = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            logMessage.append(stackTrace[i]).append(", ");
        }
        log.error("BaseException Error : " + "Error status : " + e.getStatus().toString() + ", Path = " + logMessage);
        return new BaseResponse<>(e.getStatus());
    }

}