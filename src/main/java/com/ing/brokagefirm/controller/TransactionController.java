package com.ing.brokagefirm.controller;

import com.ing.brokagefirm.service.SecurityService;
import com.ing.brokagefirm.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final SecurityService securityService;


    public TransactionController(TransactionService transactionService, SecurityService securityService) {
        this.transactionService = transactionService;
        this.securityService = securityService;
    }


    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam Long customerId, @RequestParam BigDecimal amount) {
        securityService.validateAuthorizationByCustomer(customerId);

        transactionService.deposit(customerId, amount);

        return ResponseEntity.ok("Deposit successful");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam Long customerId, @RequestParam BigDecimal amount, @RequestParam String iban) {
        securityService.validateAuthorizationByCustomer(customerId);

        transactionService.withdraw(customerId, amount, iban);

        return ResponseEntity.ok("Withdrawal successful to IBAN: " + iban);
    }

}
