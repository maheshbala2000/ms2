package com.obviousai.service;

import java.util.Date;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.obviousai.domain.Payment;
import com.obviousai.domain.PaymentDTO;
import com.obviousai.dtos.InventoryDTO;
import com.obviousai.paymentms.exceptions.ResourceNotFoundException;
import com.obviousai.repository.PaymentRepository;

@Service
public class PaymentService {

   @Autowired
   private RestTemplate restTemplate;

   @Autowired
   private PaymentRepository paymentRepository;

   @Value("${inventory.service.url}")
   private String inventoryServiceUrl;
   
   @Autowired
   ModelMapper mapper;
   
   public Payment processPayment(PaymentDTO paymentDTO) {
	   
	   Payment payment = mapper.map(paymentDTO, Payment.class);
	   payment.setPaymentDate(new Date());
	   payment.setPaymentStatus(Payment.PaymentStatus.PENDING);

       // Validate stock by calling Inventory Service using RestTemplate
	   
	   ResponseEntity<InventoryDTO> ent=null;
		try {
			ent = restTemplate.postForEntity(inventoryServiceUrl + "/validate/" + paymentDTO.getOrderId(), 
							   null, InventoryDTO.class);
			if(ent.getStatusCode().equals(HttpStatus.OK)) {
				payment.setPaymentStatus(Payment.PaymentStatus.SUCCESS);
			}
		} catch (RestClientException e) {
			payment.setPaymentStatus(Payment.PaymentStatus.FAILED);
		}
       // Publish payment success event to MQ

       return paymentRepository.save(payment);
   }

   public Payment getPaymentDetails(UUID transactionId) {
       return paymentRepository.findById(transactionId.toString())
           .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
   }
}
