package com.payments.paymentsconciliationservice.aplication;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Component
public class TestJmsListener {
    @JmsListener(destination = "${app.connections.ems.destinationnames.queue}", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String text = ((TextMessage) message).getText();
                System.out.println("Test received message: " + text);
            } else {
                System.out.println("Received non-text message: " + message);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
