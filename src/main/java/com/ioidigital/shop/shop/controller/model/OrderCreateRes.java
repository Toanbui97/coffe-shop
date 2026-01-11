package com.ioidigital.shop.shop.controller.model;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRes {

    private Long orderId;
    private Integer estimateWaitTime;
    private Double totalAmount;
}
