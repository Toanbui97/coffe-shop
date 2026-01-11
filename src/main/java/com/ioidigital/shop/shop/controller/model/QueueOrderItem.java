package com.ioidigital.shop.shop.controller.model;

import com.ioidigital.shop.constant.OrderStatus;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QueueOrderItem {
    private Long orderId;
    private List<QueueOrderStockItem> stocks;
    private OrderStatus orderStatus;
    private String userFullName;
    private Integer estimateWaitingMinus;
    private Integer position;
    private Double totalAmount;
}
