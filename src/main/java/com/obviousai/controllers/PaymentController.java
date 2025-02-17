package com.obviousai.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obviousai.domain.Payment;
import com.obviousai.domain.PaymentDTO;
import com.obviousai.service.PaymentService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/")
@EnableDiscoveryClient
public class PaymentController {

   @Autowired
   private PaymentService paymentService;

   @Transactional
   @PostMapping("payment")
   public ResponseEntity<Payment> processPayment(@RequestBody PaymentDTO paymentDTO) {
       return ResponseEntity.ok(paymentService.processPayment(paymentDTO));
   }

   @GetMapping("{transactionId}")
   public ResponseEntity<Payment> getPaymentDetails(@PathVariable UUID transactionId) {
       return ResponseEntity.ok(paymentService.getPaymentDetails(transactionId));
   }

//   @Autowired
//   private DiscoveryClient discoveryClient;
//   
//   @GetMapping("/service-instances/{applicationName}")
//   public String getServiceUrl(String serviceName) {
//	   	String url = "No instance found";
//	   	List<InstanceInfo> instancesById = discoveryClient.getInstancesById(serviceName);
//	   	if(instancesById.size()>0)
//	   		url=instancesById.get(0).getHomePageUrl();
//    	return url;
//   }

}
