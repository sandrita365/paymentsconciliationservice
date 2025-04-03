package com.payments.paymentsconciliationservice.domain.ports.output;

import com.payments.paymentsconciliationservice.domain.model.Forex;

import java.util.List;

public interface FilePollerStarterPort {

    List<Forex> ReaderFile();
}
