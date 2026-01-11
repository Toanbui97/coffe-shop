package com.ioidigital.shop.shop.controller.model;

import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QueueOrderRes {
    private Long queueId;
    private Long shopId;
    private List<QueueOrderItem> orders;
}
