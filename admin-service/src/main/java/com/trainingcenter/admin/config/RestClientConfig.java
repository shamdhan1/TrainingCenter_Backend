package com.trainingcenter.admin.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Educational demonstration configuration:
 * Demonstrates direct REST inter-service communication using Spring's RestTemplate
 * with client-side load balancing via Netflix Eureka.
 */
@Configuration
public class RestClientConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
