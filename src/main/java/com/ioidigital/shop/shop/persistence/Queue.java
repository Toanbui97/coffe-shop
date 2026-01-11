package com.ioidigital.shop.shop.persistence;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@Table(name = "queues")
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Queue {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "size")
    private Integer size;

    @Column(name = "current_size")
    @Builder.Default
    private Integer currentSize = 0;

    @Column(name = "remain_time")
    @Builder.Default
    private Integer remainTime = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "queue")
    private Set<Order> orders = new HashSet<>();
}
