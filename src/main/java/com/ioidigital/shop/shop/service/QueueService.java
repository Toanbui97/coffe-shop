package com.ioidigital.shop.shop.service;

import com.ioidigital.shop.auth.persistence.UserRepository;
import com.ioidigital.shop.constant.OrderStatus;
import com.ioidigital.shop.exception.AppErrorCodeMsg;
import com.ioidigital.shop.exception.BaseRuntimeException;
import com.ioidigital.shop.shop.controller.model.*;
import com.ioidigital.shop.shop.persistence.*;
import com.ioidigital.shop.shop.persistence.Queue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRepository queueRepository;
    private final QueueEventPublisher eventPublisher;
    private final UserRepository userRepository;

    private final Map<Long, SseEmitter> activeClients = new ConcurrentHashMap<>();
    private final ShopRepository shopRepository;


    public Queue getBestQueue(Shop shop) {
        var queues = queueRepository.findByShop(shop);
        return queues.stream()
                .filter(q ->
                        q.getCurrentSize() <= q.getSize()
                )
                .min(
                        Comparator
                                .comparing(Queue::getRemainTime)
                                .thenComparing(Queue::getCurrentSize)
                )
                .orElseThrow(() -> {
                    log.error("getBestQueue() - No available queues found.");
                    return new BaseRuntimeException(AppErrorCodeMsg.SHOP_50006);
                });
    }

    public SseEmitter streamQueueUpdates(String shopIds) {
        var emitter = new SseEmitter(Long.MAX_VALUE);
        var user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new BaseRuntimeException(AppErrorCodeMsg.AUTH_40004));

        activeClients.put(user.getId(), emitter);
        var watchedShops = parseShopIds(shopIds);

        var shops = shopRepository.findByIdInWithQueues(watchedShops);
        // Send welcome
        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data(shops.stream().flatMap(s -> s.getQueues()
                            .stream().map(q -> QueueConsumeEvent.builder()
                                    .shopId(s.getId())
                                    .queueId(q.getId())
                                    .currentSize(q.getCurrentSize())
                                    .totalSize(q.getSize())
                                    .estimateWaitMinutes(q.getRemainTime())
                                    .build())).toList()));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        BiConsumer<QueueUpdateEvent, List<Order>> listener = (updateEvent, orders) -> {
            var sortedOrders = orders.stream()
                    .filter(order -> !OrderStatus.CANCELED.equals(order.getOrderStatus())
                        && !OrderStatus.COMPLETED.equals(order.getOrderStatus()))
                    .sorted(Comparator.comparingLong(Order::getId))
                    .toList();

            updateEvent.setYourPosition(IntStream.range(0, sortedOrders.size())
                    .filter(i -> sortedOrders.get(i).getUser() != null
                            && sortedOrders.get(i).getUser().getId().equals(user.getId()))
                    .findFirst()
                    .orElse(-1));

            if (watchedShops.contains(updateEvent.getShopId())) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("queue-update")
                            .id(UUID.randomUUID().toString())
                            .data(updateEvent));
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            }
        };

        // Register listener
        watchedShops.forEach(shopId ->
                eventPublisher.addListener(shopId, listener));

        // Cleanup
        emitter.onCompletion(() -> {
            activeClients.remove(user.getId());
            watchedShops.forEach(id -> eventPublisher.removeListener(id, listener));
        });

        emitter.onTimeout(() -> {
            emitter.complete();
            activeClients.remove(user.getId());
        });

        emitter.onError(e -> {
            emitter.completeWithError(e);
            activeClients.remove(user.getId());
        });

        return emitter;
    }


    private Set<Long> parseShopIds(String param) {
        if (param == null || param.isBlank()) return Set.of();
        try {
            return Arrays.stream(param.split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            return Set.of();
        }
    }

    public QueueOrderRes getOrders(Long queueId) {
        log.info("getOrders() - queueId = {}", queueId);

        var queue = queueRepository.findOrdersInQueue(queueId)
                .orElseThrow(() -> {
                    log.error("getOrders() - Queue not found.");
                    return new BaseRuntimeException(AppErrorCodeMsg.SHOP_50009);
                });

        return QueueOrderRes.builder()
                .shopId(queue.getShop().getId())
                .queueId(queueId)
                .orders(queue.getOrders().stream()
                        .sorted(Comparator.comparingLong(Order::getId))
                        .map(order -> QueueOrderItem.builder()
                                .orderId(order.getId())
                                .orderStatus(order.getOrderStatus())
                                .totalAmount(order.getTotalAmount())
                                .userFullName(order.getUser().getFullName())
                                .stocks(order.getStocks().stream()
                                        .map(stock ->
                                            QueueOrderStockItem.builder()
                                                    .stockId(stock.getId())
                                                    .stockName(stock.getStock().getName())
                                                    .price(stock.getStock().getPrice())
                                                    .number(stock.getStockNumber())
                                                    .build()
                                        ).toList())
                                .build())
                        .toList())
                .build();
    }
}
