package com.ioidigital.shop.exception;

import org.springframework.http.HttpStatus;

public enum AppErrorCodeMsg implements BaseErrorCodeMsg {
    COMMON_10001(HttpStatus.INTERNAL_SERVER_ERROR, 10001, "Internal Server Error."),

    AUTH_40001(HttpStatus.UNAUTHORIZED, 40001, "Authentication failed."),
    AUTH_40002(HttpStatus.UNAUTHORIZED, 40002, "Concurrent login."),
    AUTH_40003(HttpStatus.FORBIDDEN, 40003, "Access denied."),
    AUTH_40004(HttpStatus.NOT_FOUND, 40004, "User not found."),
    AUTH_40005(HttpStatus.NOT_FOUND, 40005, "User invalid."),


    SHOP_50000(HttpStatus.BAD_REQUEST, 50000, "Shop id invalid."),
    SHOP_50001(HttpStatus.BAD_REQUEST, 50001, "Stock invalid."),
    SHOP_50002(HttpStatus.BAD_REQUEST, 50002, "Queue number must not be null or empty."),
    SHOP_50003(HttpStatus.BAD_REQUEST, 50003, "Queue size must not be null or empty."),
    SHOP_50004(HttpStatus.BAD_REQUEST, 50004, "Order stocks must not be null or empty."),
    SHOP_50005(HttpStatus.BAD_REQUEST, 50005, "Stock dose not belong to shop."),
    SHOP_50006(HttpStatus.BAD_REQUEST, 50006, "No available queues found."),
    SHOP_50007(HttpStatus.BAD_REQUEST, 50007, "Order not found."),
    SHOP_50008(HttpStatus.BAD_REQUEST, 50008, "User already have an order."),
    SHOP_50009(HttpStatus.BAD_REQUEST, 50009, "Queue not found."),
    SHOP_50010(HttpStatus.BAD_REQUEST, 50009, "Shop not found."),


    ;

    private final HttpStatus httpStatus;
    private final int resultCode;
    private final String resultMsg;

    AppErrorCodeMsg(HttpStatus httpStatus, int resultCode, String resultMsg) {
        this.httpStatus = httpStatus;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public AppErrorCodeMsg valueOf(final int serverResponseCode) {

        for (AppErrorCodeMsg commonErrorCodeMsg : values()) {
            if (commonErrorCodeMsg.resultCode == serverResponseCode) {
                return commonErrorCodeMsg;
            }
        }
        return null;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public int getResultCode() {
        return resultCode;
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }
}
