package com.ioidigital.shop.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseRuntimeException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final Integer resultCode;
    private final String resultMsg;

    public BaseRuntimeException(final BaseErrorCodeMsg baseErrorCodeMsg) {
        super(baseErrorCodeMsg.getResultMsg());
        this.httpStatus = baseErrorCodeMsg.getHttpStatus();
        this.resultCode = baseErrorCodeMsg.getResultCode();
        this.resultMsg = getMessage();
    }

    public BaseRuntimeException(final BaseErrorCodeMsg baseErrorCodeMsg, final String message) {
        super(message);
        this.httpStatus = baseErrorCodeMsg.getHttpStatus();
        this.resultCode = baseErrorCodeMsg.getResultCode();
        this.resultMsg = message;
    }

    public BaseRuntimeException(final Integer errorCode, final String message, final HttpStatus httpStatus) {
        super(message);
        this.resultCode = errorCode;
        this.resultMsg = message;
        this.httpStatus = httpStatus;
    }

    public BaseRuntimeException(final BaseErrorCodeMsg baseErrorCodeMsg, final String message, final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.resultCode = baseErrorCodeMsg.getResultCode();
        this.resultMsg = message;
    }

    public BaseRuntimeException(final BaseErrorCodeMsg baseErrorCodeMsg, final HttpStatus httpStatus) {
        super(baseErrorCodeMsg.getResultMsg());
        this.httpStatus = httpStatus;
        this.resultCode = baseErrorCodeMsg.getResultCode();
        this.resultMsg = baseErrorCodeMsg.getResultMsg();
    }

    public BaseRuntimeException(Exception ex) {
        super(ex);
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        resultCode = 0;
        resultMsg = "";
    }

    public BaseRuntimeException(String errorMessage) {
        super(errorMessage);
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        resultCode = 0;
        resultMsg = "";
    }
}
