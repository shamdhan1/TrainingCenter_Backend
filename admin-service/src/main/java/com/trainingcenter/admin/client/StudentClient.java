package com.trainingcenter.admin.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "student-service")
public interface StudentClient {

    @GetMapping("/api/v1/students/count")
    Long getStudentCount();
}
