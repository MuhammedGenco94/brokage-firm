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

    /**
     * List assets of a customer.
     *
     * @param customerId customer id
     * @return list of assets
     */
    public List<Asset> listAssets(Long customerId) {
        return assetRepository.findByCustomerId(customerId);
    }

    /**
     * Find asset by customer id and asset name.
     *
     * @param customerId customer id
     * @param assetName  asset name
     * @return asset
     */
    public Asset findByCustomerIdAndAssetName(Long customerId, String assetName) {
        return assetRepository.findByCustomerIdAndAssetName(customerId, assetName)
                .orElse(null);
    }

    /**
     * Save asset.
     */
    public void save(Asset asset) {
        assetRepository.save(asset);
    }

}
