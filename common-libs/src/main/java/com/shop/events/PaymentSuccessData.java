package com.shop.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class PaymentSuccessData {
    private String orderId;
    private String transactionId;
    private double amount;
    private String currency;
    private String paymentMethod;
    private String customerId;
    private String status;
}