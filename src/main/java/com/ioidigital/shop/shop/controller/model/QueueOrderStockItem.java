package com.ioidigital.shop.shop.controller.model;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QueueOrderStockItem {
    private Long stockId;
    private Integer number;
    private String stockName;
    private Double price;
}
