package com.ing.brokagefirm.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * The Asset represents the financial holdings (assets or stocks) that a customer owns,
 * including both cash (TRY) and stock shares.
 */
@Entity
@Table(name = "assets")
@Data
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Customer is the owner of the asset.
     */
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /**
     * Asset is the name of the stock, customer wants to buy.
     */
    private String assetName;

    /**
     * Size refers to the total quantity of a given asset that the customer owns.
     * <br>
     * This represents the entire stock holding for a customer in their portfolio, irrespective of how much they are currently able to trade.
     */
    @Column(precision = 19, scale = 4)
    private BigDecimal size;

    /**
     * Usable-size describes how much of an asset is available for trading.
     * <br>
     * This is the portion of the customer’s asset that is not tied up in any pending orders or other restricted actions.
     * This is the amount they can freely trade or use for transactions.
     */
    @Column(precision = 19, scale = 4)
    private BigDecimal usableSize;

}
