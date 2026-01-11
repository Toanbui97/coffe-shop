package com.ioidigital.shop.shop.service;

import com.ioidigital.shop.shop.controller.model.QueueUpdateEvent;
import com.ioidigital.shop.shop.persistence.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

@Component
public class QueueEventPublisher {
    private final Map<Long, List<BiConsumer<QueueUpdateEvent, List<Order>>>> listeners = new ConcurrentHashMap<>();

    public void publish(QueueUpdateEvent update, List<Order> orders) {
        var subs = listeners.getOrDefault(update.getShopId(), Collections.emptyList());
        subs.forEach(consumer -> consumer.accept(update, orders));
    }

    public void addListener(Long shopId, BiConsumer<QueueUpdateEvent, List<Order>> consumer) {
        listeners.computeIfAbsent(shopId, k -> new CopyOnWriteArrayList<>()).add(consumer);
    }

    public void removeListener(Long shopId, BiConsumer<QueueUpdateEvent, List<Order>> consumer) {
        var list = listeners.get(shopId);
        if (list != null) {
            list.remove(consumer);
            if (list.isEmpty()) listeners.remove(shopId);
        }
    }
}
