package com.ioidigital.shop.shop.persistence;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;


@Getter
@Setter
@Entity
@Builder
@Table(name = "stocks")
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Stock {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "estimate_time_minute")
    private Integer estimateTimeMinute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

}
