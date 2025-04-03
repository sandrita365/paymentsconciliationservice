package com.payments.paymentsconciliationservice.infrastructure.adapters.output;

import com.payments.paymentsconciliationservice.domain.ports.output.ClearTodaysRecordsPort;
import com.payments.paymentsconciliationservice.domain.ports.output.ForexRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Service
public class ClearTodaysRecordsAdapter implements ClearTodaysRecordsPort {

    ForexRepositoryPort forexRepositoryPort;

    @Value("${app.ClearTodaysRecords.TimeIntervalMillis}")
    private long interval;

    public ClearTodaysRecordsAdapter(ForexRepositoryPort forexRepositoryPort) {
        this.forexRepositoryPort = forexRepositoryPort;
    }
    private final Timer timer = new Timer();
    @Override
    public void scheduleTask() {
        // Define the start time (2018-08-27 00:00:00 GMT-6)
        ZonedDateTime startTime = ZonedDateTime.of(
                2018, 8, 27, 0, 0, 0, 0,
                ZoneId.of("America/Mexico_City") // GMT-6 Time Zone
        );

        Date startDate = Date.from(startTime.toInstant());

        // Schedule the task to run every 24 hours
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    forexRepositoryPort.deleteAll();
                    ;
                    log.info("Task executed at: " + new Date());
                } catch (RuntimeException e) {
                    log.error("Error:: ", e);
                }
            }
        }, startDate, interval);

        log.info("Task scheduled for: " + startDate + " and will repeat every " + (interval / 3600000) + " hours.");
    }
}
