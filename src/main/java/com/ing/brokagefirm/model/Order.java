package com.ing.brokagefirm.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private String assetName;

    @Enumerated(EnumType.STRING)
    private Side side;

    @Column(precision = 19, scale = 4)
    private BigDecimal size;

    @Column(precision = 19, scale = 4)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createDate;

}
