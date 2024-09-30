package com.ing.brokagefirm.service;

import com.ing.brokagefirm.model.*;
import com.ing.brokagefirm.model.dto.OrderDTO;
import com.ing.brokagefirm.repository.OrderRepository;
import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class OrderService {

    private final CustomerService customerService;
    private final AssetService assetService;
    private final OrderRepository orderRepository;

    public static final String TRY = "TRY";


    public OrderService(CustomerService customerService, AssetService assetService, OrderRepository orderRepository) {
        this.customerService = customerService;
        this.assetService = assetService;
        this.orderRepository = orderRepository;
    }

    /**
     * Create an order.
     *
     * @param orderDTO order dto
     * @return created order
     */
    public Order createOrder(OrderDTO orderDTO) {
        if (orderDTO.getSize().compareTo(BigDecimal.ZERO) <= 0 || orderDTO.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Size and price must be greater than zero!");
            throw new IllegalArgumentException("Size and price must be greater than zero!");
        }

        // BUY side
        if (Side.BUY.equals(orderDTO.getSide())) {
            // As customer can only trade with TRY balance.
            Asset tryAsset = assetService.findByCustomerIdAndAssetName(orderDTO.getCustomerId(), TRY);

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
            Asset stockAsset = assetService.findByCustomerIdAndAssetName(orderDTO.getCustomerId(), orderDTO.getAssetName());

            if (stockAsset == null || stockAsset.getUsableSize().compareTo(orderDTO.getSize()) < 0) {
                throw new IllegalArgumentException("Insufficient shares for '%s' stockAsset to sell".formatted(orderDTO.getAssetName()));
            }

            stockAsset.setUsableSize(stockAsset.getUsableSize().subtract(orderDTO.getSize()));
            assetService.save(stockAsset);
        }

        return orderRepository.save(getOrder(orderDTO));
    }

    /**
     * List orders of a customer.
     *
     * @param customerId customer id
     * @return list of orders
     */
    public List<Order> listOrders(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    /**
     * List orders of a customer between given dates.
     *
     * @param customerId customer id
     * @param startDate  start date
     * @param endDate    end date
     * @return list of orders
     */
    public List<Order> listOrders(Long customerId, @Nonnull String startDate, @Nonnull String endDate) {
        LocalDateTime startDateTime = LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDateTime = LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return orderRepository.findByCustomerIdAndCreateDateBetween(customerId, startDateTime, endDateTime);
    }

    /**
     * Cancel a pending order.
     *
     * @param orderId order id
     */
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));

        if (!Status.PENDING.equals(order.getStatus())) {
            throw new IllegalArgumentException("Only pending orders can be canceled");
        }

        // BUY side
        if (Side.BUY.equals(order.getSide())) {
            Asset tryAsset = assetService.findByCustomerIdAndAssetName(order.getCustomer().getId(), TRY);
            BigDecimal refund = order.getPrice().multiply(order.getSize());

            tryAsset.setUsableSize(tryAsset.getUsableSize().add(refund));

            assetService.save(tryAsset);
        }
        // SELL side
        else if (Side.SELL.equals(order.getSide())) {
            Asset stockAsset = assetService.findByCustomerIdAndAssetName(order.getCustomer().getId(), order.getAssetName());

            stockAsset.setUsableSize(stockAsset.getUsableSize().add(order.getSize()));

            assetService.save(stockAsset);
        }

        order.setStatus(Status.CANCELLED);
        orderRepository.save(order);
    }


    private Order getOrder(OrderDTO orderDTO) {
        Customer customer = customerService.findById(orderDTO.getCustomerId());

        Order order = new Order();
        order.setCustomer(customer);
        order.setAssetName(orderDTO.getAssetName());
        order.setSide(orderDTO.getSide());
        order.setSize(orderDTO.getSize());
        order.setPrice(orderDTO.getPrice());
        order.setStatus(Status.PENDING);
        order.setCreateDate(LocalDateTime.now());
        return order;
    }

    public Order findById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
    }

    @Transactional
    public void matchOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!Status.PENDING.equals(order.getStatus())) {
            throw new IllegalArgumentException("Only pending orders can be matched");
        }

        Customer customer = order.getCustomer();
        Asset tryAsset = assetService.findByCustomerIdAndAssetName(customer.getId(), "TRY");

        // BUY side
        if (Side.BUY.equals(order.getSide())) {
            BigDecimal totalCost = order.getSize().multiply(order.getPrice());
            if (tryAsset.getUsableSize().compareTo(totalCost) < 0) {
                throw new IllegalArgumentException("Insufficient TRY balance for matching");
            }

            tryAsset.setSize(tryAsset.getSize().subtract(totalCost));
            assetService.save(tryAsset);

            Asset boughtAsset = assetService.findByCustomerIdAndAssetName(customer.getId(), order.getAssetName());
            if (boughtAsset == null) {
                boughtAsset = new Asset(customer, order.getAssetName(), BigDecimal.ZERO, BigDecimal.ZERO);
            }

            boughtAsset.setSize(boughtAsset.getSize().add(order.getSize()));
            boughtAsset.setUsableSize(boughtAsset.getUsableSize().add(order.getSize()));
            assetService.save(boughtAsset);

        }
        // SELL side
        else if (Side.SELL.equals(order.getSide())) {
            Asset sellingAsset = assetService.findByCustomerIdAndAssetName(customer.getId(), order.getAssetName());

            if (sellingAsset.getUsableSize().compareTo(order.getSize()) < 0) {
                throw new IllegalArgumentException("Insufficient stock size to sell");
            }

            sellingAsset.setSize(sellingAsset.getSize().subtract(order.getSize()));
            assetService.save(sellingAsset);

            BigDecimal totalIncome = order.getSize().multiply(order.getPrice());
            tryAsset.setUsableSize(tryAsset.getUsableSize().add(totalIncome));
            tryAsset.setSize(tryAsset.getSize().add(totalIncome));
            assetService.save(tryAsset);
        }

        order.setStatus(Status.MATCHED);
        orderRepository.save(order);
    }

}
