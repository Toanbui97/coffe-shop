package com.ioidigital.shop.shop.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QueueRepository extends JpaRepository<Queue, Long> {

    List<Queue> findByShop_IdIn(List<Long> shopIds);

    List<Queue> findByShop_Id(Long shopId);

    List<Queue> findByShop(Shop shop);

    @Query("""
            SELECT DISTINCT q FROM Queue q
                                 JOIN FETCH q.shop
                                 JOIN FETCH q.orders o
                                 JOIN FETCH o.stocks s
                                 JOIN FETCH s.stock
                                 JOIN FETCH o.user 
                             WHERE q.id = :queueId
                               AND o.orderStatus IN ('WAITING', 'PROCESSING')
            """)
    Optional<Queue> findOrdersInQueue(Long queueId);
}
