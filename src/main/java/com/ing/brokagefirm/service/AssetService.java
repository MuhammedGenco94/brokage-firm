package com.ing.brokagefirm.service;

import com.ing.brokagefirm.model.Asset;
import com.ing.brokagefirm.repository.AssetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {
    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public List<Asset> listAssets(Long customerId) {
        return assetRepository.findByCustomerId(customerId);
    }

    public Asset findByCustomerIdAndAssetName(Long customerId, String assetName) {
        return assetRepository.findByCustomerIdAndAssetName(customerId, assetName)
                .orElse(null);
    }

    public void save(Asset asset) {
        assetRepository.saveAndFlush(asset);
    }

}
