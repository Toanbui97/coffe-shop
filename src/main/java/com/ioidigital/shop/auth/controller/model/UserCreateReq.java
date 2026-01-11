package com.ioidigital.shop.auth.controller.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateReq {

    private String fullName;
    private String phoneNumber;
    private String address;
    private String username;
    private String password;
}
