package com.payments.paymentsconciliationservice.infrastructure.config;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XmlMapperConfig {
    @Bean
    public XmlMapper xmlMapper() {
        return new XmlMapper();
    }
}
