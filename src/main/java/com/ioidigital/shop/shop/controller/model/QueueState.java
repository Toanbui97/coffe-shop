package com.ioidigital.shop.shop.controller.model;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QueueState {
    private Long shopId;
    private Long queueId;
    private int currentSize;
    private double avgWaitMinutes;
}
