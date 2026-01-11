package com.ioidigital.shop.shop.controller.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QueueConsumeEvent {
    private Long shopId;
    private Long queueId;
    private int currentSize;
    private int totalSize;
    private double estimateWaitMinutes;
}
