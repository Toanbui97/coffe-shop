package com.ioidigital.shop.shop.controller.model;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShopQueueItem {
    private Long queueId;
    private Integer queueSize;
    private Integer currentSize;
    private Integer remainingTime;
}
