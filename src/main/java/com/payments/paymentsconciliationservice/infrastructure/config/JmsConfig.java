package com.payments.paymentsconciliationservice.infrastructure.config;

import com.tibco.tibjms.TibjmsConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

@Slf4j
@Configuration
public class JmsConfig {
    @Value("${app.connections.ems.destinationnames.host}")
    private String url;
    @Value("${app.connections.ems.destinationnames.password}")
    private String password;
    @Value("${app.connections.ems.destinationnames.user}")
    private String user;

    @Bean
    public ConnectionFactory connectionFactory() {
        log.info("In ConnectionFactory method: ");
        try {
            TibjmsConnectionFactory connectionFactory = new TibjmsConnectionFactory();
            connectionFactory.setServerUrl(url);
            connectionFactory.setUserName(user); // Usuario
            connectionFactory.setUserPassword(password); // Contraseña

            SingleConnectionFactory singleConnectionFactory = new SingleConnectionFactory(connectionFactory);
            singleConnectionFactory.setReconnectOnException(true);
            return singleConnectionFactory;

        } catch (JMSException e) {
            log.error("Error creating JMS connection: " + e.getMessage());
            throw new RuntimeException("JMS Connection failed", e); // Stop app
        }


    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency("1-5"); // Adjust thread pool size if needed
        return factory;
    }
}
