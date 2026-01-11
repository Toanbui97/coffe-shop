package com.ioidigital.shop.shop.controller.model;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QueueJoinRes {
    private String orderId;
    private Long queueId;
    private int position;
    private double estimatedWaitMinutes;
}
