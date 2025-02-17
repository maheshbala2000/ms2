package com.obviousai.service;

import java.util.Date;
import java.util.UUID;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.obviousai.Config.ValidateProduct;
import com.obviousai.domain.Payment;
import com.obviousai.domain.PaymentDTO;
import com.obviousai.dtos.InventoryDTO;
import com.obviousai.paymentms.exceptions.ResourceNotFoundException;
import com.obviousai.repository.PaymentRepository;

@Service
public class PaymentService {

   Logger logger = LogManager.getLogger(getClass());
	
   @Autowired
   private RestTemplate restTemplate;
   @Autowired
   @Lazy
   private EurekaClient eurekaClient;
   
   @Autowired
   ValidateProduct validateProduct;

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
			InstanceInfo info = eurekaClient.getApplication("inventory-service").
					getInstances().get(0);
			logger.info("Host/appname INFO::: "+info.getHostName()+":"+info.getPort()+"/"+info.getAppName());
			// TODO: Doesn't work (404 error)
			//			String res = validateProduct.validateAndLockStock();
			logger.info("Now calling inventory service to validate & lock the stock.");
			ent = restTemplate.postForEntity(inventoryServiceUrl + "/validate/" + paymentDTO.getOrderId(), 
					null, InventoryDTO.class);

			if(ent.getStatusCode().equals(HttpStatus.OK)) {
				payment.setPaymentStatus(Payment.PaymentStatus.SUCCESS);
			}
		} catch (RestClientException e) {
			logger.error("Exception occured while processing payment: " + e.getMessage());
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
