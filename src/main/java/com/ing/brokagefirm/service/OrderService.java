package com.ing.brokagefirm.service;

import com.ing.brokagefirm.model.*;
import com.ing.brokagefirm.model.dto.OrderDTO;
import com.ing.brokagefirm.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
public class OrderService {

    private final CustomerService customerService;
    private final AssetService assetService;
    private final OrderRepository orderRepository;

    public OrderService(CustomerService customerService, AssetService assetService, OrderRepository orderRepository) {
        this.customerService = customerService;
        this.assetService = assetService;
        this.orderRepository = orderRepository;
    }


    public Order createOrder(OrderDTO orderDTO) {
        if (orderDTO.getSize().compareTo(BigDecimal.ZERO) <= 0 || orderDTO.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Size and price must be greater than zero!");
            throw new RuntimeException("Size and price must be greater than zero!");
        }

        Customer customer = customerService.findById(orderDTO.getCustomerId());

        // BUY side
        if (Side.BUY.equals(orderDTO.getSide())) {
            // As customer can only trade with TRY balance.
            Asset tryAsset = assetService.findByCustomerIdAndAssetName(orderDTO.getCustomerId(), "TRY");

            BigDecimal totalBuyCost = orderDTO.getSize().multiply(orderDTO.getPrice());

            if (tryAsset == null || tryAsset.getUsableSize().compareTo(totalBuyCost) < 0) {
                throw new IllegalArgumentException("Insufficient TRY balance for this order");
            }

            tryAsset.setUsableSize(tryAsset.getUsableSize().subtract(totalBuyCost));
            assetService.save(tryAsset);
        }
        // SELL side
        else if (Side.SELL.equals(orderDTO.getSide())) {
            // As customer can sell any owned shares.
            Asset asset = assetService.findByCustomerIdAndAssetName(orderDTO.getCustomerId(), orderDTO.getAssetName());

            if (asset == null || asset.getUsableSize().compareTo(orderDTO.getSize()) < 0) {
                throw new IllegalArgumentException("Insufficient shares for this asset to sell");
            }

            asset.setUsableSize(asset.getUsableSize().subtract(orderDTO.getSize()));
            assetService.save(asset);
        }

        // Save order
        Order order = new Order();
        order.setCustomer(customer);
        order.setAssetName(orderDTO.getAssetName());
        order.setSide(orderDTO.getSide());
        order.setSize(orderDTO.getSize());
        order.setPrice(orderDTO.getPrice());
        order.setStatus(Status.PENDING);
        order.setCreateDate(LocalDateTime.now());

        return orderRepository.saveAndFlush(order);
    }
}
