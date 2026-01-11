package com.ioidigital.shop.shop.controller.model;

import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateReq {
    private Long shopId;
    private List<OrderStockItem> stocks;
}
