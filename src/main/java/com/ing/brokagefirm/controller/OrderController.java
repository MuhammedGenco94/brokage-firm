package com.ing.brokagefirm.controller;

import com.ing.brokagefirm.model.Order;
import com.ing.brokagefirm.model.dto.OrderDTO;
import com.ing.brokagefirm.service.OrderService;
import com.ing.brokagefirm.service.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final SecurityService securityService;


    public OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }


    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody OrderDTO orderDTO) {
        securityService.validateAuthorizationByCustomer(orderDTO.getCustomerId());

        Order order = orderService.createOrder(orderDTO);

        log.info("Order No:'{}' created successfully", order.getId());
        return ResponseEntity.ok("Order created successfully");
    }

//    @GetMapping("/list")
//    public List<Order> listOrders(@RequestParam Long customerId, @RequestParam String startDate, @RequestParam String endDate) {
//        return orderService.listOrders(customerId, startDate, endDate);
//    }
//
//    @DeleteMapping("/cancel/{orderId}")
//    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
//        orderService.cancelOrder(orderId);
//
//        log.info("Order No:'{}' canceled successfully", orderId);
//        return ResponseEntity.ok("Order No:'" + orderId + "' canceled successfully");
//    }

}
