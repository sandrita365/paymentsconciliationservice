package com.payments.paymentsconciliationservice;

import com.payments.paymentsconciliationservice.infrastructure.output.SendMessageGBS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SendMessageGBSTest {
    SendMessageGBS sendMessageGBS;
    @Mock
    JmsTemplate jmsTemplate;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        sendMessageGBS = new SendMessageGBS(jmsTemplate);
    }

    //
    @Test
    void MessageSenderOKTest() {
        String destino = "miQueueTest";
        String msg = "Prueba exitosa";
        doNothing().when(jmsTemplate).send(eq(destino), any(MessageCreator.class));

        // Ejecutar el código que usa jmsTemplate.send()
        // jmsTemplate.send(destino, session -> session.createTextMessage(msg));
        sendMessageGBS.MessageSender(destino, msg);

        verify(jmsTemplate, times(1)).send(eq(destino), any(MessageCreator.class));

    }

    @Test
    void MessageSender_ShouldThrowException_WhenSendingFails() {
        // Simular fallo en el JMS
        doThrow(new JmsException("Fallo en JMS") {
        }).when(jmsTemplate).send(anyString(), any());

        // Verificar que el método lanza una excepción
        assertThrows(JmsException.class, () -> {
            sendMessageGBS.MessageSender("testQueue", "testMessage");
        });
    }

    @Test
    void MessageSender_ShouldThrowException_WhenDestinationIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            sendMessageGBS.MessageSender(null, "testMessage");
        });
    }

    @Test
    void MessageSender_ShouldThrowException_WhenMessageIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            sendMessageGBS.MessageSender("testQueue", null);
        });
    }

    @Test
    void MessageSender_ShouldThrowException_WhenMessageIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            sendMessageGBS.MessageSender("testQueue", "");
        });
    }

    @Test
    void MessageSender_ShouldThrowException_WhenDestinationIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            sendMessageGBS.MessageSender("testQueue", "");
        });
    }
}
