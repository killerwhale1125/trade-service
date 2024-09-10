package carrot.market.common.baseutil;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static carrot.market.common.baseutil.BaseResponseStatus.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"code", "message", "result"})
public class BaseResponse<T> {
    private final String message;
    private final String code;
    private T result;

    public BaseResponse() {
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
    }

    /**
     * 요청 성공
     * @param result
     */
    public BaseResponse(T result) {
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
        this.result = result;
    }

    /**
     * 요청 실패
     * @param status
     */
    public BaseResponse(BaseResponseStatus status) {
        this.message = status.getMessage();
        this.code = status.getCode();
    }

}