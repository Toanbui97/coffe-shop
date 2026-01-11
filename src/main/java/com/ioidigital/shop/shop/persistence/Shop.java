package com.ioidigital.shop.shop.persistence;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.locationtech.jts.geom.Point;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@Table(name = "shops")
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(columnDefinition = "geography(Point, 4326)")
    private Point point;

    @Column(name = "opening_time")
    private Timestamp openingTime;

    @Column(name = "closing_time")
    private Timestamp closingTime;

    @Transient
    private Double distanceMeters;

    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Queue> queues = new HashSet<>();

    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Stock> menu = new HashSet<>();

}
