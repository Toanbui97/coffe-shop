package com.ioidigital.shop.util;

import com.ioidigital.shop.exception.BaseRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class ResponseFactory {

    public ResponseEntity<BaseResponse> success(HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(
                BaseResponse.builder()
                .httpStatus(httpStatus)
                .resultCode(httpStatus.value())
                .resultMsg("success")
                .build());
    }

    public ResponseEntity<BaseResponse> success(HttpStatus httpStatus,
                                                Long resourceId) {

        return ResponseEntity.status(httpStatus).body(
                BaseResponse.builder()
                        .httpStatus(httpStatus)
                        .resultCode(httpStatus.value())
                        .resultMsg("success")
                        .resourceId(resourceId)
                        .build());
    }

    public <E extends BaseRuntimeException> ResponseEntity<BaseResponse> fail(E exception) {
        return ResponseEntity.status(exception.getHttpStatus())
                .body(BaseResponse.builder()
                        .httpStatus(exception.getHttpStatus())
                        .resultCode(exception.getResultCode())
                        .resultMsg(exception.getResultMsg())
                        .build());
    }

    public ResponseEntity<BaseResponse> fail(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.builder()
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .resultCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .resultMsg(exception.getMessage())
                        .build());
    }

    public <E extends BaseRuntimeException, T> ResponseEntity<BaseDataResponse<T>> fail(E exception, T data) {

        return ResponseEntity.status(exception.getHttpStatus())
                .body(BaseDataResponse.<T>build()
                        .httpStatus(exception.getHttpStatus())
                        .resultCode(exception.getResultCode())
                        .resultMsg(exception.getResultMsg())
                        .data(data)
                        .build());
    }

    public <T> ResponseEntity<BaseDataResponse<T>> success(HttpStatus httpStatus, T data) {
        return ResponseEntity.status(httpStatus)
                .body(BaseDataResponse.<T>build()
                        .httpStatus(httpStatus)
                        .resultCode(httpStatus.value())
                        .resultMsg("success")
                        .data(data)
                        .build());
    }

    public <T> ResponseEntity<BaseDataResponse<T>> success(HttpStatus httpStatus, T data, Long resourceId) {
        return ResponseEntity.status(httpStatus)
                .body(BaseDataResponse.<T>build()
                        .httpStatus(httpStatus)
                        .resultCode(httpStatus.value())
                        .resultMsg("success")
                        .resourceId(resourceId)
                        .data(data)
                        .build());
    }
}
