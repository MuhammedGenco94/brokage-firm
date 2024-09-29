package com.ing.brokagefirm.controller;

import com.ing.brokagefirm.model.dto.CustomerDTO;
import com.ing.brokagefirm.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody CustomerDTO customerDTO) {
        customerService.registerCustomer(customerDTO);

        log.info("Customer '{}' registered successfully", customerDTO.getName());
        return ResponseEntity.ok("Customer '" + customerDTO.getName() + "' registered successfully");
    }

}
