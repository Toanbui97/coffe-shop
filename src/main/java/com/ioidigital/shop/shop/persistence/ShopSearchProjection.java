package com.ioidigital.shop.shop.persistence;

import java.sql.Timestamp;

public interface ShopSearchProjection {

    Long getId();
    String getAddress();
    String getPhoneNumber();
    String getEmail();
    Timestamp getOpeningTime();
    Timestamp getClosingTime();
    Double getDistanceMeters();

}
