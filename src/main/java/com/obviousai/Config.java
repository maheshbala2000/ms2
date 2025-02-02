package com.obviousai;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests(auth -> auth
//                .antMatchers("/payment/**").authenticated() // Require authentication for payment endpoints
//                .antMatchers("/inventory/**")//.hasRole("ADMIN") // Require ADMIN role for inventory endpoints
                .anyRequest().permitAll() // Allow all other requests
            );

        return http.build();
    }

    @Bean
    public RestTemplate restTemplate() {
       return new RestTemplate();
    }
    
    @Bean
    public ModelMapper modelMapper() {
    	return new ModelMapper();
    }
}
