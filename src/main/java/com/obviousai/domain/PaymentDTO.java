package com.obviousai.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class PaymentDTO {
    @Id
    private String id;
    private String orderId;
    private BigDecimal totalAmount;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    
    private String paymentDate;

   // Getters and setters

   public enum PaymentStatus {
	   @JsonProperty("SUCCESS")
       SUCCESS,
       @JsonProperty("FAILED")
       FAILED,
       @JsonProperty("PENDING")
       PENDING
   }
}
