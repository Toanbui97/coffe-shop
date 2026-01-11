package com.ioidigital.shop.auth.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ioidigital.shop.constant.Role;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResp {
    private String accessToken;
//    private String refreshToken;
    private Long userId;
    private String username;
    private String fullName;
    private String avatar;
    private Role role;
}
