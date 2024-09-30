package com.ing.brokagefirm.service;

import com.ing.brokagefirm.model.Customer;
import com.ing.brokagefirm.model.Order;
import com.ing.brokagefirm.model.Role;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private final CustomerService customerService;
    private final OrderService orderService;


    public SecurityService(CustomerService customerService, OrderService orderService) {
        this.customerService = customerService;
        this.orderService = orderService;
    }


    public void validateAuthorizationByCustomer(Long customerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Customer currentCustomer = customerService.findByName(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Current authenticated customer not found"));

        if (currentCustomer.getRole().equals(Role.ADMIN)) {
            return;
        }

        if (currentCustomer.getRole().equals(Role.USER)) {
            if (!customerId.equals(currentCustomer.getId())) {
                throw new AuthorizationServiceException("You can only access and manipulate your own data");
            }
        }
    }

    public void validateAuthorizationByOrder(Long orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Customer currentCustomer = customerService.findByName(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Current authenticated customer not found"));

        if (currentCustomer.getRole().equals(Role.ADMIN)) {
            return;
        }

        Order order = orderService.findById(orderId);

        if (currentCustomer.getRole().equals(Role.USER)) {
            if (!order.getCustomer().getId().equals(currentCustomer.getId())) {
                throw new IllegalArgumentException("You can only access and manipulate your own data.");
            }
        }
    }

}
