package com.ioidigital.shop.shop.service;

import com.ioidigital.shop.shop.controller.model.OrderCreateReq;
import com.ioidigital.shop.shop.controller.model.OrderCreateRes;

public interface OrderService {

    OrderCreateRes createOrder(Long shopId,
                               OrderCreateReq createReq);

    void cancelOrder(Long orderId);

    void serveOrder(Long orderId);

    void completeOrder(Long orderId);
}
