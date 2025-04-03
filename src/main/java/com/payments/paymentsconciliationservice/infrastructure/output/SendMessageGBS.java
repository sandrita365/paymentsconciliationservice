package com.payments.paymentsconciliationservice.infrastructure.output;

import com.payments.paymentsconciliationservice.domain.ports.input.MqServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Message;

@Slf4j
@Service
public class SendMessageGBS implements MqServicePort {

    private final JmsTemplate jmsTemplate;

    public SendMessageGBS(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void MessageSender(String destination, String msg) {
        if (destination == null || msg == null || destination.isEmpty() || msg.isEmpty()) {
            log.error("The parameters can't be null or empty.");
            throw new IllegalArgumentException("The parameters can't be null or empty.");

        }
        jmsTemplate.send(destination, session -> {
            Message message = session.createTextMessage(msg);
            return message;
        });
    }
}
