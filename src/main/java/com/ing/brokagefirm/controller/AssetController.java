package com.ing.brokagefirm.controller;

import com.ing.brokagefirm.model.Asset;
import com.ing.brokagefirm.service.AssetService;
import com.ing.brokagefirm.service.SecurityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;
    private final SecurityService securityService;

    public AssetController(AssetService assetService, SecurityService securityService) {
        this.assetService = assetService;
        this.securityService = securityService;
    }

    @GetMapping("/list")
    public List<Asset> listAssets(@RequestParam Long customerId) {
        securityService.validateAuthorizationByCustomer(customerId);

        return assetService.listAssets(customerId);
    }

}
