package com.ing.brokagefirm.controller;

import com.ing.brokagefirm.model.Asset;
import com.ing.brokagefirm.service.AssetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping("/list")
    public List<Asset> listAssets(@RequestParam Long customerId) {
        return assetService.listAssets(customerId);
    }

}
