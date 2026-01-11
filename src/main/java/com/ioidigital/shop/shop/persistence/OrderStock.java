package com.ioidigital.shop.shop.persistence;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@Entity
@Builder
@Table(name = "order_stocks")
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class OrderStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @Column(name = "stock_number")
    private Integer stockNumber;
}
