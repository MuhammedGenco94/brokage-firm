package com.ing.brokagefirm.controller;

import com.ing.brokagefirm.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {


    private final OrderService orderService;

    public AdminController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("match/{orderId}")
    public ResponseEntity<String> matchOrder(@PathVariable Long orderId) {
        orderService.matchOrder(orderId);

        return ResponseEntity.ok("Order matched successfully");
    }
}
