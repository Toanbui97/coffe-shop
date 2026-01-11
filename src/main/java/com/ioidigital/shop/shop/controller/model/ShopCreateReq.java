package com.ioidigital.shop.shop.controller.model;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShopCreateReq {
    private String address;
    private String phoneNumber;
    private String email;
    private Double longitude;
    private Double latitude;
    private Timestamp openingTime;
    private Timestamp closingTime;
    private Integer queueNumber;
    private Integer queueSize;
    private List<ShopStockItem> stocks;
}
