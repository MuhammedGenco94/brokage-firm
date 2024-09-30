package com.ing.brokagefirm.controller;

import com.ing.brokagefirm.model.Asset;
import com.ing.brokagefirm.model.Customer;
import com.ing.brokagefirm.service.AssetService;
import com.ing.brokagefirm.service.CustomerService;
import com.ing.brokagefirm.service.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final CustomerService customerService;
    private final AssetService assetService;
    private final SecurityService securityService;

    public TransactionController(CustomerService customerService, AssetService assetService, SecurityService securityService) {
        this.customerService = customerService;
        this.assetService = assetService;
        this.securityService = securityService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam Long customerId, @RequestParam BigDecimal amount) {
        securityService.validateAuthorizationByCustomer(customerId);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body("Deposit amount must be positive");
        }
        Customer customer = customerService.findById(customerId);

        Asset tryAsset = assetService.findByCustomerIdAndAssetName(customerId, "TRY");

        if (tryAsset == null) {
            tryAsset = new Asset();
            tryAsset.setCustomer(customer);
            tryAsset.setAssetName("TRY");
            tryAsset.setSize(BigDecimal.ZERO);
            tryAsset.setUsableSize(BigDecimal.ZERO);
        }

        tryAsset.setSize(tryAsset.getSize().add(amount));
        tryAsset.setUsableSize(tryAsset.getUsableSize().add(amount));

        assetService.save(tryAsset);

        return ResponseEntity.ok("Deposit successful");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam Long customerId, @RequestParam BigDecimal amount, @RequestParam String iban) {
        securityService.validateAuthorizationByCustomer(customerId);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body("Withdrawal amount must be positive");
        }

        Asset tryAsset = assetService.findByCustomerIdAndAssetName(customerId, "TRY");

        if (tryAsset == null || tryAsset.getUsableSize().compareTo(amount) < 0) {
            return ResponseEntity.badRequest().body("Insufficient TRY balance for withdrawal");
        }

        tryAsset.setSize(tryAsset.getSize().subtract(amount));
        tryAsset.setUsableSize(tryAsset.getUsableSize().subtract(amount));
        assetService.save(tryAsset);

        return ResponseEntity.ok("Withdrawal successful to IBAN: " + iban);
    }

}
