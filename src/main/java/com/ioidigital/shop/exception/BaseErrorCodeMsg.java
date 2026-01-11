package com.ioidigital.shop.exception;

import org.springframework.http.HttpStatus;

public interface BaseErrorCodeMsg {

  HttpStatus getHttpStatus();

  int getResultCode();

  String getResultMsg();
}
