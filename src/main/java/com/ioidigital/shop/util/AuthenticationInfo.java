package com.ioidigital.shop.util;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationInfo {
    private Long userId;
    private String fullName;
    private String sessionId;
    private Long authorityId;
    private Set<Long> dataAccess;
}
