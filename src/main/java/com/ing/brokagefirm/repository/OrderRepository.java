package com.ing.brokagefirm.repository;

import com.ing.brokagefirm.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByCustomerIdAndCreateDateBetween(Long customerId, LocalDateTime parse, LocalDateTime parse1);

}
