package com.ioidigital.shop.shop.controller.model;

import com.ioidigital.shop.constant.QueueUpdateType;
import com.ioidigital.shop.shop.persistence.Order;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QueueUpdateEvent {
    private Long shopId;
    private Long queueId;
    private int currentSize;
    private double estimateWaitMinutes;
    private int yourPosition;
    private QueueUpdateType changeType;
    private Timestamp timestamp;
}
