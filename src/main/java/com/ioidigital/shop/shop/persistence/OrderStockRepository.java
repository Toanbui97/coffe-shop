package com.ioidigital.shop.shop.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStockRepository extends JpaRepository<OrderStock, Integer> {
}
