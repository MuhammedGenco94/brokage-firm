package com.ing.brokagefirm.service;

import com.ing.brokagefirm.model.Asset;
import com.ing.brokagefirm.model.Customer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final CustomerService customerService;
    private final AssetService assetService;


    public TransactionService(CustomerService customerService, AssetService assetService) {
        this.customerService = customerService;
        this.assetService = assetService;
    }


    public void deposit(Long customerId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
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
    }

    public void withdraw(Long customerId, BigDecimal amount, String iban) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        Asset tryAsset = assetService.findByCustomerIdAndAssetName(customerId, "TRY");

        if (tryAsset == null || tryAsset.getUsableSize().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient TRY balance for withdrawal");
        }

        tryAsset.setSize(tryAsset.getSize().subtract(amount));
        tryAsset.setUsableSize(tryAsset.getUsableSize().subtract(amount));
        assetService.save(tryAsset);
    }
}
