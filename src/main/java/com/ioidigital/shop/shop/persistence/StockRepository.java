package com.ioidigital.shop.shop.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Set<Stock> findByIdIn(Set<Long> stocks);

    List<Stock> findByShop_IdIn(List<Long>  shopIds);
}
