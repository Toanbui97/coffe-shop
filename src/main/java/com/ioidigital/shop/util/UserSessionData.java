package com.ioidigital.shop.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionData {
    private String sessionId;
    private String ipAddress;
    private Long queue;
}
