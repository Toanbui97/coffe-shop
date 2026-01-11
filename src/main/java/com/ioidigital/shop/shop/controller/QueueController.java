package com.ioidigital.shop.shop.controller;

import com.ioidigital.shop.shop.controller.model.QueueOrderRes;
import com.ioidigital.shop.shop.service.QueueService;
import com.ioidigital.shop.util.BaseDataResponse;
import com.ioidigital.shop.util.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/queues")
@RequiredArgsConstructor
public class QueueController {

    private final QueueService queueService;
    private final ResponseFactory responseFactory;


    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamQueueUpdates(@RequestParam(required = false) String shopIds) {

        return queueService.streamQueueUpdates(shopIds);

    }

    @GetMapping("/{queueId}/orders")
    @PreAuthorize("hasAnyAuthority('OWNER', 'OPERATOR')")
    public ResponseEntity<BaseDataResponse<QueueOrderRes>> getOrderInQueue(@PathVariable("queueId") Long queueId) {

        var orders = queueService.getOrders(queueId);

        return responseFactory.success(HttpStatus.OK, orders);
    }

}
