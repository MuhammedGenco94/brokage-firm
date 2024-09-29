package com.ing.brokagefirm.service;

import com.ing.brokagefirm.model.Customer;
import com.ing.brokagefirm.model.Role;
import com.ing.brokagefirm.model.dto.CustomerDTO;
import com.ing.brokagefirm.repository.CustomerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void registerCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setPassword(new BCryptPasswordEncoder().encode(customerDTO.getPassword()));
        customer.setRole(Role.USER);
        customer.setBalance(BigDecimal.ZERO);

        customerRepository.saveAndFlush(customer);
    }

    /**
     * @return the total number of customers in the database.
     */
    public long countAllCustomers() {
        return customerRepository.count();
    }

    public boolean noDatabaseCustomers() {
        return countAllCustomers() == 0;
    }

    public Optional<Customer> findByName(String name) {
        return customerRepository.findByName(name);
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + id));
    }

}
