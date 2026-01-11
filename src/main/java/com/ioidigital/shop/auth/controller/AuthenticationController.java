package com.ioidigital.shop.auth.controller;

import com.ioidigital.shop.auth.controller.model.AuthenticationReq;
import com.ioidigital.shop.auth.controller.model.AuthenticationResp;
import com.ioidigital.shop.auth.controller.model.UserCreateReq;
import com.ioidigital.shop.auth.service.AuthenticationService;
import com.ioidigital.shop.constant.Role;
import com.ioidigital.shop.util.BaseDataResponse;
import com.ioidigital.shop.util.BaseResponse;
import com.ioidigital.shop.util.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final ResponseFactory responseFactory;
    private final AuthenticationService authenticationService;

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<BaseDataResponse<AuthenticationResp>> authenticateUser(@RequestBody AuthenticationReq authenticationReq) {

        var authentication = authenticationService.authenticateUser(authenticationReq);

        return responseFactory.success(HttpStatus.OK, authentication);
    }

    @PostMapping("/register")
    ResponseEntity<BaseResponse> registerUser(@RequestBody UserCreateReq userCreateReq) {
        var userId = authenticationService.createUser(userCreateReq, Role.CUSTOMER);
        return responseFactory.success(HttpStatus.OK, userId);

    }

    @PostMapping("/barista")
    @PreAuthorize("hasAuthority('OWNER')")
    ResponseEntity<BaseResponse> createBarista(@RequestBody UserCreateReq userCreateReq) {
        var userId = authenticationService.createUser(userCreateReq, Role.OPERATOR);
        return responseFactory.success(HttpStatus.OK, userId);
    }
}
