package com.ioidigital.shop.auth.controller.model;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationReq {
    private String username;
    private String password;
}
