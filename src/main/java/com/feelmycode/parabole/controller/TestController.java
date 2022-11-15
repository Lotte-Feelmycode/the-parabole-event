package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.client.ParaboleServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/event")
@RequiredArgsConstructor
public class TestController {

    private final ParaboleServiceClient paraboleServiceClient;

    @GetMapping("/test")
    public void testController(@RequestParam Long productId){
        System.out.println(paraboleServiceClient.getProduct(productId));
    }
}
