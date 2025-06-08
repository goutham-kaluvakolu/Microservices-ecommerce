package com.ecommers.payment_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/payment")
public class PaymentServiceController {
    @GetMapping("/ping")
    public String getPayment() {
        return "Payment service is running";
    }
}