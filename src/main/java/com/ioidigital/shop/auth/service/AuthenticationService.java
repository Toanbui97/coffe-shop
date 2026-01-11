package com.ioidigital.shop.auth.service;

import com.ioidigital.shop.auth.controller.model.AuthenticationReq;
import com.ioidigital.shop.auth.controller.model.AuthenticationResp;
import com.ioidigital.shop.auth.controller.model.UserCreateReq;
import com.ioidigital.shop.constant.Role;

public interface AuthenticationService {

    AuthenticationResp authenticateUser(AuthenticationReq authenticationReq);

    Long createUser(UserCreateReq userCreateReq,
                    Role customer);
}
