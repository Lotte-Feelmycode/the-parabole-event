package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.client.ParaboleServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/event/health")
@RestController
public class HealthCheckController {

    private final ParaboleServiceClient paraboleServiceClient;

    @GetMapping()
    public String healthCheck() {
        if (paraboleServiceClient.healthCheck()) {
            return "Hello World";
        } else {
            return "T.T";
        }
    }
}
