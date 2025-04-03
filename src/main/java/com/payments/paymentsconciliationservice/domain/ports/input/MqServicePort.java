package com.payments.paymentsconciliationservice.domain.ports.input;

public interface MqServicePort {

    void MessageSender(String destination, String message);

}
