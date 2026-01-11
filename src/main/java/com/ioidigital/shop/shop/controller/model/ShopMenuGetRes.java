package com.ioidigital.shop.shop.controller.model;

import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShopMenuGetRes {
    private Long shopId;
    private List<ShopStockItem> stocks;
}
