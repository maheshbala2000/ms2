package com.obviousai;

import org.modelmapper.ModelMapper;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableFeignClients
public class Config {

    @Bean
    public RestTemplate restTemplate() {
       return new RestTemplate();
    }
    
    @Bean
    public ModelMapper modelMapper() {
    	return new ModelMapper();
    }
    
    @Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
    @FeignClient(name="inventory-service", url="http://localhost:8082/")
    public interface ValidateProduct {
        @PostMapping("/validate/{productId}")
        String validateAndLockStock();
    }
}
