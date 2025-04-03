package com.payments.paymentsconciliationservice.domain.ports.output;

import com.payments.paymentsconciliationservice.domain.model.Forex;
import com.payments.paymentsconciliationservice.infrastructure.persistencie.entity.ForexEntity;

import java.util.List;

public interface ForexRepositoryPort {

    ForexEntity guardar(Forex forex);

    List<ForexEntity> listar();

    void deleteAll();
}