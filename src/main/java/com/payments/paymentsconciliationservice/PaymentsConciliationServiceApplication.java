package com.payments.paymentsconciliationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

@SpringBootApplication
@EnableJms
public class PaymentsConciliationServiceApplication {
    private JmsTemplate jms;

    public static void main(String[] args) {
        SpringApplication.run(PaymentsConciliationServiceApplication.class, args);
    }

}
