package com.payments.paymentsconciliationservice.aplication;

import com.payments.paymentsconciliationservice.domain.ports.output.ClearTodaysRecordsPort;
import com.payments.paymentsconciliationservice.domain.ports.output.ForexRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Service
public class ClearTodaysRecordsCU implements CommandLineRunner{
    private final ClearTodaysRecordsPort clearTodaysRecordsPort;



    public ClearTodaysRecordsCU(ClearTodaysRecordsPort clearTodaysRecordsPort) {
        this.clearTodaysRecordsPort = clearTodaysRecordsPort;
    }




    @Override
    public void run(String... args) throws Exception {
        //execute();
    }
    public void execute() {
        log.info("Executing ClearTodaysRecordsCU...");
        try {
            clearTodaysRecordsPort.scheduleTask();
            log.info("Task scheduled successfully.");
        } catch (Exception e) {
            log.error("Error scheduling task", e);
        }
    }
}
