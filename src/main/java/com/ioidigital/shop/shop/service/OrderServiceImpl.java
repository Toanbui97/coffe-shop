package com.ioidigital.shop.shop.service;

import com.ioidigital.shop.auth.persistence.UserRepository;
import com.ioidigital.shop.constant.OrderStatus;
import com.ioidigital.shop.constant.QueueUpdateType;
import com.ioidigital.shop.exception.AppErrorCodeMsg;
import com.ioidigital.shop.exception.BaseRuntimeException;
import com.ioidigital.shop.shop.controller.model.OrderCreateReq;
import com.ioidigital.shop.shop.controller.model.OrderCreateRes;
import com.ioidigital.shop.shop.controller.model.OrderStockItem;
import com.ioidigital.shop.shop.controller.model.QueueUpdateEvent;
import com.ioidigital.shop.shop.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ShopRepository shopRepository;
    private final StockRepository stockRepository;
    private final QueueRepository queueRepository;

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final QueueService queueService;
    private final OrderStockRepository orderStockRepository;

    private final QueueEventPublisher queueEventPublisher;

    @Override
    @Transactional
    public OrderCreateRes createOrder(Long shopId,
                                      OrderCreateReq createReq) {

        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("createOrder() - shopId = {}, username = {},  body = {}", shopId, username, createReq);

        var shop = shopRepository.findById(shopId).orElseThrow(() ->
                new BaseRuntimeException(AppErrorCodeMsg.SHOP_50000));

        var user = userRepository.findByUsername(username).orElseThrow(() ->
                new BaseRuntimeException(AppErrorCodeMsg.AUTH_40004));

        if (CollectionUtils.isEmpty(createReq.getStocks())) {
            log.error("createOrder() - stocks can not be empty");
            throw new BaseRuntimeException(AppErrorCodeMsg.SHOP_50004);
        }

        orderRepository.findByUserAndOrderStatusIn(user, List.of(OrderStatus.WAITING, OrderStatus.PROCESSING))
                .ifPresent(order -> {
                    log.error("createOrder() - User already have an order");
                    throw new BaseRuntimeException(AppErrorCodeMsg.SHOP_50008);
                });

        var stockMap = stockRepository.findByIdIn(createReq.getStocks()
                        .stream().map(OrderStockItem::getStockId)
                        .collect(Collectors.toSet()))
                .stream()
                .collect(Collectors.toMap(Stock::getId, Function.identity()));

        if (createReq.getStocks().stream().anyMatch(e -> stockMap.get(e.getStockId()) == null)) {
            log.error("createOrder() - stock dose not belong to shop.");
            throw new BaseRuntimeException(AppErrorCodeMsg.SHOP_50004);
        }

        var order = new Order();
        order.setShop(shop);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.WAITING);

        var orderStocks = new ArrayList<OrderStock>();
        var totalAmount = 0.0;
        var estimateTimeMinutes = 0;

        for (var s : createReq.getStocks()) {
            var stock = stockMap.get(s.getStockId());
            totalAmount = totalAmount + stock.getPrice() * s.getNumber();
            estimateTimeMinutes = estimateTimeMinutes + stock.getEstimateTimeMinute() * s.getNumber();
            orderStocks.add(OrderStock.builder()
                    .order(order)
                    .stock(stockMap.get(s.getStockId()))
                    .stockNumber(s.getNumber())
                    .build());
        }

        order.setTotalAmount(totalAmount);
        order.setEstimateTimeMinute(estimateTimeMinutes);
        order.setStocks(orderStocks);

        var queue = queueService.getBestQueue(shop);
        order.setQueue(queue);
        queue.getOrders().add(order);

        queue.setCurrentSize(queue.getCurrentSize() + 1);
        queue.setRemainTime(queue.getRemainTime() + estimateTimeMinutes);


        orderRepository.save(order);
        orderStockRepository.saveAll(order.getStocks());
        queueRepository.save(queue);

        queueEventPublisher.publish(QueueUpdateEvent.builder()
                        .shopId(shopId)
                        .queueId(queue.getId())
                        .currentSize(queue.getCurrentSize())
                        .yourPosition(queue.getCurrentSize())
                        .estimateWaitMinutes(queue.getRemainTime())
                        .changeType(QueueUpdateType.NEW_ORDER)
                        .timestamp(Timestamp.from(Instant.now()))
                        .build(),
                new ArrayList<>(queue.getOrders()));

        return OrderCreateRes.builder()
                .estimateWaitTime(queue.getRemainTime())
                .orderId(order.getId())
                .totalAmount(order.getTotalAmount())
                .build();
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        log.info("cancelOrder() - orderId = {}", orderId);

        var user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new BaseRuntimeException(AppErrorCodeMsg.AUTH_40004));

        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BaseRuntimeException(AppErrorCodeMsg.SHOP_50007));

        var shop = order.getShop();

        if (!user.getId().equals(order.getUser().getId())) {
            throw new BaseRuntimeException(AppErrorCodeMsg.AUTH_40003);
        }

        order.setOrderStatus(OrderStatus.CANCELED);


        var queue = order.getQueue();
        queue.setRemainTime(queue.getRemainTime() - order.getEstimateTimeMinute());
        queue.setCurrentSize(queue.getCurrentSize() - 1);

        queueRepository.save(queue);
        orderRepository.save(order);

        queueEventPublisher.publish(QueueUpdateEvent.builder()
                        .shopId(shop.getId())
                        .queueId(queue.getId())
                        .currentSize(queue.getCurrentSize())
                        .estimateWaitMinutes(queue.getRemainTime())
                        .changeType(QueueUpdateType.CANCEL_ORDER)
                        .timestamp(Timestamp.from(Instant.now()))
                        .build(),
                new ArrayList<>(queue.getOrders()));
    }


    @Override
    @Transactional
    public void completeOrder(Long orderId) {
        log.info("completeOrder() - orderId = {}", orderId);

        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BaseRuntimeException(AppErrorCodeMsg.SHOP_50007));

        var shop = order.getShop();
        order.setOrderStatus(OrderStatus.COMPLETED);

        var queue = order.getQueue();
        queue.setRemainTime(queue.getRemainTime() - order.getEstimateTimeMinute());
        queue.setCurrentSize(queue.getCurrentSize() - 1);
        orderRepository.save(order);
        queueRepository.save(queue);

        queueEventPublisher.publish(QueueUpdateEvent.builder()
                        .shopId(shop.getId())
                        .queueId(queue.getId())
                        .currentSize(queue.getCurrentSize())
                        .estimateWaitMinutes(queue.getRemainTime())
                        .changeType(QueueUpdateType.COMPLETE_ORDER)
                        .timestamp(Timestamp.from(Instant.now()))
                        .build(),
                new ArrayList<>(queue.getOrders()));
    }

    @Override
    @Transactional
    public void serveOrder(Long orderId) {
        log.info("serveOrder() - orderId = {}", orderId);

        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BaseRuntimeException(AppErrorCodeMsg.SHOP_50007));

        var shop = order.getShop();
        var queue = order.getQueue();
        order.setOrderStatus(OrderStatus.PROCESSING);

        orderRepository.save(order);
        queueEventPublisher.publish(QueueUpdateEvent.builder()
                        .shopId(shop.getId())
                        .queueId(queue.getId())
                        .currentSize(queue.getCurrentSize())
                        .estimateWaitMinutes(queue.getRemainTime())
                        .changeType(QueueUpdateType.PROCESSING_ORDER)
                        .timestamp(Timestamp.from(Instant.now()))
                        .build(),
                new ArrayList<>(queue.getOrders()));
    }
}
