package com.ioidigital.shop.exception;

import com.ioidigital.shop.util.BaseResponse;
import com.ioidigital.shop.util.ResponseFactory;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestApiExceptionHandler {

    private final ResponseFactory responseFactory;

    @ExceptionHandler(BaseRuntimeException.class)
    public ResponseEntity<BaseResponse> handlerBaseRunTimeException(BaseRuntimeException baseRuntimeException) {
        log.error("handlerBaseRunTimeException().", baseRuntimeException);
        return responseFactory.fail(baseRuntimeException);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<BaseResponse> handleAccessDeniedException(AuthorizationDeniedException authorizationDeniedException) {
        var forbiddenEx = new BaseRuntimeException(AppErrorCodeMsg.AUTH_40003);
        log.error("handleAccessDeniedException().", forbiddenEx);
        return responseFactory.fail(forbiddenEx);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handlerException(Exception exception) {
        log.error("handlerException().", exception);
        return responseFactory.fail(exception);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<BaseResponse> handleNoResourceFoundException(NoResourceFoundException exception) {
        log.error("handleNoResourceFoundException().", exception);
        return responseFactory.fail(exception);
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<BaseResponse> handleServletException(ServletException exception) {
        log.error("handleServletException().", exception);
        return responseFactory.fail(exception);
    }

}
