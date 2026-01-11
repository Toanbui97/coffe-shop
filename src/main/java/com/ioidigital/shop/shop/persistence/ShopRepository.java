package com.ioidigital.shop.shop.persistence;

import com.ioidigital.shop.shop.controller.model.ShopStockItem;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    @Query(value = """
                    SELECT 
                                     s.id,
                                                 s.address,
                                                 s.phone_number AS phoneNumber,
                                                 s.email,
                                                s.opening_time AS openingTime,
                                                            s.closing_time as closingTime,
                                              CASE
                                                  WHEN :longitude IS NOT NULL AND :latitude IS NOT NULL
                                                  THEN ST_Distance(
                                                      s.point,
                                                      ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography
                                                  )
                                                  ELSE NULL
                                              END AS distanceMeters
                                          FROM shops s
                                          ORDER BY
                                              CASE
                                                  WHEN :longitude IS NOT NULL AND :latitude IS NOT NULL
                                                  THEN ST_Distance(
                                                      s.point,
                                                      ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography
                                                  )
                                                  ELSE s.id
                                              END
            """,
            countQuery = "SELECT COUNT(id) FROM shops",
            nativeQuery = true)
    Page<ShopSearchProjection> findPageShops(
            @Param("longitude") Double longitude,
            @Param("latitude") Double latitude,
            Pageable pageable);

    @Query("""
        SELECT s from Shop s
                join fetch s.queues
                where s.id in :ids
        """)
    List<Shop> findByIdInWithQueues(Collection<Long> ids);

    @Query("""
        SELECT s from Shop s
                join fetch s.menu
                where s.id = :shopId
        """)
    Optional<Shop> findByIdWithStocks(Long shopId);
}
