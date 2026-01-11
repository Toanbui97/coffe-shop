package com.ioidigital.shop.shop.controller;

import com.ioidigital.shop.shop.controller.model.OrderCreateReq;
import com.ioidigital.shop.shop.controller.model.OrderCreateRes;
import com.ioidigital.shop.shop.service.OrderService;
import com.ioidigital.shop.util.BaseDataResponse;
import com.ioidigital.shop.util.BaseResponse;
import com.ioidigital.shop.util.ResponseFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final ResponseFactory responseFactory;
    private final OrderService orderService;

    @PostMapping()
    public ResponseEntity<BaseDataResponse<OrderCreateRes>> createOrder(@RequestBody OrderCreateReq createReq) {

        var id = orderService.createOrder(createReq.getShopId(), createReq);

        return responseFactory.success(HttpStatus.OK, id);
    }

    @PutMapping("/{orderId}/cancel")
    @PreAuthorize("hasAuthority('OPERATOR')")
    public ResponseEntity<BaseResponse> cancelOrder(@PathVariable("orderId") Long orderId) {

        orderService.cancelOrder(orderId);

        return responseFactory.success(HttpStatus.OK);
    }

    @PutMapping("/{orderId}/complete")
    @PreAuthorize("hasAnyAuthority('OWNER', 'OPERATOR')")
    public ResponseEntity<BaseResponse> completeOrder(@PathVariable("orderId") Long orderId) {
        orderService.completeOrder(orderId);

        return responseFactory.success(HttpStatus.OK);
    }

    @PutMapping("/{orderId}/serve")
    @PreAuthorize("hasAnyAuthority('OWNER', 'OPERATOR')")
    public ResponseEntity<BaseResponse> serveOrder(@PathVariable("orderId") Long orderId) {

        orderService.serveOrder(orderId);

        return responseFactory.success(HttpStatus.OK);
    }


}
