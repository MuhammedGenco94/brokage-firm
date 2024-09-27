package com.ing.brokagefirm.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * The Order class represents an individual stock order placed by a customer (either a buy or sell order).
 */
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

    /**
     * Asset is the name of the stock customer wants to buy.
     */
    private String assetName;

    /**
     * Side represents whether the order is a buy or sell order.
     */
    @Enumerated(EnumType.STRING)
    private Side side;

    /**
     * Size represents how many shares or assets, customer wants to buy.
     */
    @Column(precision = 19, scale = 4)
    private BigDecimal size;

    /**
     * Price represents how much, customer wants to pay for per share.
     */
    @Column(precision = 19, scale = 4)
    private BigDecimal price;

    /**
     * The current status of the order, indicating whether it is pending, matched, or cancelled.
     */
    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * The timestamp indicating when the order was created.
     */
    private LocalDateTime createDate;

}
