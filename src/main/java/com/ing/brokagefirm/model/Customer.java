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

    /**
     * The unique identifier used by the customer to log in to their account.
     */
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * Password is the secret code that a customer uses to access their account.
     */
    private String encodedPassword;

    /**
     * Role represents the customer's role in the system, which can be either USER or ADMIN.
     */
    @Enumerated(EnumType.STRING)
    private Role role;

}
