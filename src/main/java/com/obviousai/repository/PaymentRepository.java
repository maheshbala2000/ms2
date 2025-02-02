package com.obviousai.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obviousai.domain.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    
    // Custom query method to find payment by transaction Id
    Optional<Payment> findById(String productId);
}
