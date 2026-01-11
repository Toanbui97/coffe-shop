package com.ioidigital.shop.shop.service;

import com.ioidigital.shop.auth.persistence.UserRepository;
import com.ioidigital.shop.constant.OrderStatus;
import com.ioidigital.shop.exception.AppErrorCodeMsg;
import com.ioidigital.shop.exception.BaseRuntimeException;
import com.ioidigital.shop.shop.controller.model.*;
import com.ioidigital.shop.shop.persistence.*;
import com.ioidigital.shop.util.PagedData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;
    private final QueueRepository queueRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Override
    @Transactional(readOnly = true)
    public PagedData<PageShopItem> getShops(Pageable pageable,
                                            Double longitude,
                                            Double latitude) {
        log.info("getShops() - pageable: {}, longitude: {}, latitude: {}", pageable, longitude, latitude);

        var pageShops = shopRepository.findPageShops(longitude, latitude, pageable);

        var queueMap = queueRepository.findByShop_IdIn(pageShops.getContent()
                        .stream()
                        .map(ShopSearchProjection::getId)
                        .toList())
                .stream()
                .collect(Collectors.groupingBy(e -> e.getShop().getId()));


        return PagedData.<PageShopItem>builder()
                .pageNo(pageShops.getNumber())
                .elementPerPage(pageShops.getNumberOfElements())
                .totalPages(pageShops.getTotalPages())
                .totalElements(pageShops.getTotalElements())
                .elementList(pageShops.getContent().stream().map(e -> PageShopItem.builder()
                                .id(e.getId())
                                .address(e.getAddress())
                                .email(e.getEmail())
                                .phoneNumber(e.getPhoneNumber())
                                .openingTime(e.getOpeningTime())
                                .closingTime(e.getClosingTime())
                                .distanceMeters(e.getDistanceMeters())
                                .queues(queueMap.getOrDefault(e.getId(), List.of())
                                        .stream()
                                        .map(q -> ShopQueueItem.builder()
                                                .queueId(q.getId())
                                                .queueSize(q.getSize())
                                                .currentSize(q.getCurrentSize())
                                                .remainingTime(q.getRemainTime())
                                                .build())
                                        .toList())
                                .build())
                        .toList())
                .build();
    }

    @Override
    public ShopMenuGetRes getShopMenu(Long shopId) {
        log.info("getShopMenu() - {}", shopId);

        var shop = shopRepository.findByIdWithStocks(shopId)
                .orElseThrow(() -> new BaseRuntimeException(AppErrorCodeMsg.SHOP_50010));

        return ShopMenuGetRes.builder()
                .shopId(shop.getId())
                .stocks(shop.getMenu().stream().map(s ->
                        ShopStockItem.builder()
                                .name(s.getName())
                                .price(s.getPrice())
                                .estimateTimeMinute(s.getEstimateTimeMinute())
                                .build())
                        .toList())
                .build();
    }

    @Override
    @Transactional
    public Long createShop(ShopCreateReq createReq) {
        log.info("createShop() - body = {}", createReq);

        var shop = Shop.builder()
                .address(createReq.getAddress())
                .phoneNumber(createReq.getPhoneNumber())
                .email(createReq.getEmail())
                .openingTime(createReq.getOpeningTime())
                .closingTime(createReq.getClosingTime())
                .build();

        var point = geometryFactory.createPoint(new Coordinate(createReq.getLongitude(), createReq.getLatitude()));
        point.setSRID(4326);

        shop.setPoint(point);

        if (CollectionUtils.isEmpty(createReq.getStocks())) {
            log.error("createShop() - stocks can not be empty");
            throw new BaseRuntimeException(AppErrorCodeMsg.SHOP_50001);
        }

        var stocks = createReq.getStocks().stream()
                .map(s -> Stock.builder()
                        .name(s.getName())
                        .price(s.getPrice())
                        .estimateTimeMinute(s.getEstimateTimeMinute())
                        .shop(shop)
                        .build())
                .collect(Collectors.toSet());
        shop.setMenu(stocks);

        if (createReq.getQueueNumber() == null || createReq.getQueueNumber() == 0) {
            log.error("createShop() - queueNumber must not be null or zero");
            throw new BaseRuntimeException(AppErrorCodeMsg.SHOP_50002);
        }

        if (createReq.getQueueSize() == null || createReq.getQueueSize() == 0) {
            log.error("createShop() - queueSize must not be null or zero");
            throw new BaseRuntimeException(AppErrorCodeMsg.SHOP_50003);
        }

        Set<Queue> queues = new HashSet<>(createReq.getQueueNumber());
        for (int i = 0; i < createReq.getQueueNumber(); i++) {
            queues.add(Queue.builder()
                    .shop(shop)
                    .size(createReq.getQueueSize())
                    .build());
        }
        shop.setQueues(queues);

        shopRepository.save(shop);

        return shop.getId();
    }


    @Override
    @Transactional
    public Long updateShop(Long shopId,
                           ShopUpdateReq updateReq) {

        log.info("updateShop() - id = {}, body = {}", shopId, updateReq);

        var shop = shopRepository.findById(shopId).orElseThrow(() ->
                new BaseRuntimeException(AppErrorCodeMsg.SHOP_50000));

        shop.setPhoneNumber(updateReq.getPhoneNumber());
        shop.setEmail(updateReq.getEmail());
        shop.setOpeningTime(updateReq.getOpeningTime());
        shop.setClosingTime(updateReq.getClosingTime());

        if (CollectionUtils.isEmpty(updateReq.getStocks())) {
            log.error("updateShop() - stocks can not be empty");
            throw new BaseRuntimeException(AppErrorCodeMsg.SHOP_50001);
        }

        shop.getMenu().clear();

        updateReq.getStocks().stream()
                .map(s -> Stock.builder()
                        .name(s.getName())
                        .price(s.getPrice())
                        .estimateTimeMinute(s.getEstimateTimeMinute())
                        .shop(shop)
                        .build())
                .forEach(shop.getMenu()::add);

        if (updateReq.getQueueNumber() == null || updateReq.getQueueNumber() == 0) {
            log.error("updateShop() - queueNumber must not be null or zero");
            throw new BaseRuntimeException(AppErrorCodeMsg.SHOP_50002);
        }

        if (updateReq.getQueueSize() == null || updateReq.getQueueSize() == 0) {
            log.error("updateShop() - queueSize must not be null or zero");
            throw new BaseRuntimeException(AppErrorCodeMsg.SHOP_50003);
        }

        shop.getQueues().clear();
        IntStream.range(0, updateReq.getQueueNumber())
                .mapToObj(i -> Queue.builder()
                        .shop(shop)
                        .size(updateReq.getQueueSize())
                        .build())
                .forEach(shop.getQueues()::add);

        shopRepository.save(shop);

        return shop.getId();
    }



}
