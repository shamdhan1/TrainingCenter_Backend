package com.trainingcenter.admin.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "trainer-service")
public interface TrainerClient {

    @GetMapping("/api/v1/trainers/count")
    Long getTrainerCount();
}
