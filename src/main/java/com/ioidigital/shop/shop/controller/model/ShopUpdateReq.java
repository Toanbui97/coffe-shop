package com.ioidigital.shop.shop.controller.model;

import lombok.*;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShopUpdateReq {

    private String phoneNumber;
    private String email;
    private Timestamp openingTime;
    private Timestamp closingTime;
    private Integer queueNumber;
    private Integer queueSize;
    private Set<ShopStockItem> stocks;
}
