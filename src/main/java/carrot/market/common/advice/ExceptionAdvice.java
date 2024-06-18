package carrot.market.common.advice;


import carrot.market.exception.*;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static carrot.market.common.HttpStatusResponseEntity.*;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<HttpStatus> memberNotFoundException() {
        return RESPONSE_NOT_FOUND;
    }

    @ExceptionHandler(UnAuthorizedAccessException.class)
    public ResponseEntity<HttpStatus> unAuthorizedAccessException() {
        return RESPONSE_FORBIDDEN;
    }

    /**
     * @Valid 유효성 검사 실패
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> validationNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(e.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> categoryNotFoundException(CategoryNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AreaInfoNotDefinedException.class)
    public ResponseEntity<String> areaInfoNotDefinedException(AreaInfoNotDefinedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<HttpStatus> postNotFoundException() {
        return RESPONSE_NOT_FOUND;
    }

    @ExceptionHandler(UnAuthenticatedAccessException.class)
    public ResponseEntity<HttpStatus> unAuthenticatedAccessException() {
        return RESPONSE_UNAUTHORIZED;
    }

    @ExceptionHandler(PasswordNotMatchedException.class)
    public ResponseEntity<HttpStatus> passwordNotMatchedException() {
        return RESPONSE_BAD_REQUEST;
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<HttpStatus> fileSizeLimitExceededException() {
        return RESPONSE_PAYLOAD_TOO_LARGE;
    }
}
