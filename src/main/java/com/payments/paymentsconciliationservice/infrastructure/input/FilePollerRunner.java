package com.payments.paymentsconciliationservice.infrastructure.input;

import com.payments.paymentsconciliationservice.domain.ports.input.ProcessFileCUPort;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FilePollerRunner implements CommandLineRunner {

    private final ProcessFileCUPort processFileCUPort;

    public FilePollerRunner(ProcessFileCUPort processFileCUPort) {
        this.processFileCUPort = processFileCUPort;
    }

    @Override
    public void run(String... args) throws Exception {
        processFileCUPort.startMonitoring();
    }
}
