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
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private String assetName;

    @Enumerated(EnumType.STRING)
    private Side side;

    private BigDecimal size;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createDate;

}
