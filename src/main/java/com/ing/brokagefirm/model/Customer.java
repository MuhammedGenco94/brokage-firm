package com.ing.brokagefirm.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customers")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String password;

    /**
     * Balance represents the total amount of cash (TRY) that a customer has in their account.
     */
    @Column(precision = 19, scale = 4)
    private BigDecimal balance;

}
