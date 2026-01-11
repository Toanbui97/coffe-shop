package com.ioidigital.shop.shop.persistence;

import com.ioidigital.shop.auth.persistence.User;
import com.ioidigital.shop.constant.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByUserAndOrderStatusIn(User user, List<OrderStatus> orderStatuses);
}
