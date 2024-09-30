package com.ing.brokagefirm.service;

import com.ing.brokagefirm.model.Asset;
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
    private final AssetService assetService;

    public CustomerService(CustomerRepository customerRepository, AssetService assetService) {
        this.customerRepository = customerRepository;
        this.assetService = assetService;
    }

    public void registerCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setEncodedPassword(new BCryptPasswordEncoder().encode(customerDTO.getPassword()));
        customer.setRole(Role.USER);

        Customer savedCustomer = customerRepository.save(customer);

        Asset tryAsset = new Asset();
        tryAsset.setCustomer(savedCustomer);
        tryAsset.setAssetName("TRY");
        tryAsset.setSize(BigDecimal.ZERO);
        tryAsset.setUsableSize(BigDecimal.ZERO);

        assetService.save(tryAsset);
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
