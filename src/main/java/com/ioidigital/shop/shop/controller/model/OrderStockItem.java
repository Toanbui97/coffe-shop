package com.ioidigital.shop.shop.controller.model;


import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderStockItem {

    private Long stockId;
    private Integer number;
}
