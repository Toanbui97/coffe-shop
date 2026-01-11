package com.ioidigital.shop.shop.controller.model;

import lombok.*;


@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShopStockItem {

    private String name;
    private Double price;
    private Integer estimateTimeMinute;
}
