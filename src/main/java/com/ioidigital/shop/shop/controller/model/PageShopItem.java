package com.ioidigital.shop.shop.controller.model;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageShopItem {
    private Long id;
    private String address;
    private String phoneNumber;
    private String email;
    private Timestamp openingTime;
    private Timestamp closingTime;
    private Double distanceMeters;
    private List<Long> queueIds;
}
