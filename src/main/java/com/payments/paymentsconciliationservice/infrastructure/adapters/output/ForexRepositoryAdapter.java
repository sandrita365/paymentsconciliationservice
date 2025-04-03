package com.payments.paymentsconciliationservice.infrastructure.adapters.output;

import com.payments.paymentsconciliationservice.domain.model.Forex;
import com.payments.paymentsconciliationservice.domain.ports.output.ForexRepositoryPort;
import com.payments.paymentsconciliationservice.infrastructure.persistencie.ForexJpaRepository;
import com.payments.paymentsconciliationservice.infrastructure.persistencie.entity.ForexEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class ForexRepositoryAdapter implements ForexRepositoryPort {
    private final ForexJpaRepository forexJpaRepository;

    public ForexRepositoryAdapter(ForexJpaRepository forexJpaRepository) {
        this.forexJpaRepository = forexJpaRepository;
    }


    @Override
    public ForexEntity guardar(Forex forex) {
        log.info(" guardar::::::");
        if (forex == null) {
            log.error("Forex can't be null");
            throw new RuntimeException("Forex can't be null.");
        }
        validarForex(forex);

        try {
            log.info("Saving ForexEntity: {}", forex);
            return forexJpaRepository.save(mapToEntity(forex));
        } catch (Exception e) {
            log.error("Error saving ForexEntity: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save the Forex entity", e);

        }
    }

    private void validarForex(Forex forex) {
        if (forex.getCreditDebitIndicator() == null ||
                forex.getTransactionReference() == null ||
                forex.getAccountNumber() == null ||
                forex.getBankReference() == null ||
                forex.getValueDate() == null ||
                forex.getCurrency() == null ||
                forex.getTransactionAmount() == null) {

            log.error("No field should be null.");
            throw new IllegalArgumentException("No field should be null.");
        }


        if (forex.getCreditDebitIndicator().length() > 1) {
            log.error("Indicator must be 1 character max");
            throw new IllegalArgumentException("Indicator must be 1 character max");
        }

        if (forex.getTransactionReference().length() > 17) {
            log.error("TransactionReference must be 17 characters max.");
            throw new IllegalArgumentException("TransactionReference must be 17 characters max.");
        }


        if (forex.getAccountNumber().length() > 35) {
            log.error("AccountNumber must be 35 characters max.");
            throw new IllegalArgumentException("AccountNumber must be 35 characters max.");
        }
        if (forex.getBankReference().length() > 16) {
            log.error("BankReference must be 16 characters max.");
            throw new IllegalArgumentException("BankReference must be 16 characters max.");
        }


        if (forex.getValueDate().length() > 8) {
            log.error("ValueDate must be 8 characters max.");
            throw new IllegalArgumentException("ValueDate must be 8 characters max.");
        }

        if (forex.getCurrency().length() != 3) {
            log.error("Currency must be exactly 3 characters.");
            throw new IllegalArgumentException("Currency must be 3 characters.");
        }

        if (forex.getTransactionAmount().length() > 31) {
            log.error("TransactionAmount must be 31 characters max.");
            throw new IllegalArgumentException("TransactionAmount must be 31 characters max.");
        }
    }

    @Override
    public List<ForexEntity> listar() {
        return forexJpaRepository.findAll();
    }

    @Override
    public void deleteAll() {
        log.info("Input:: deleteAll");
        try {

            forexJpaRepository.deleteAll();
            log.info("Output:: deleteAll");
        } catch (Exception e) {
            log.error("Error occurred while clearing the table:",e);
            throw new RuntimeException("Failed to clear the table.");

        }
    }

    private ForexEntity mapToEntity(Forex forex) {
        ForexEntity forexEntity = new ForexEntity();
        forexEntity.setIndicator(forex.getCreditDebitIndicator());
        forexEntity.setTransactionReference(forex.getTransactionReference());
        forexEntity.setAccountNumber(forex.getAccountNumber());
        forexEntity.setBankReference(forex.getBankReference());
        forexEntity.setValueDate(forex.getValueDate());
        forexEntity.setCurrency(forex.getCurrency());
        forexEntity.setTransactionAmount(forex.getTransactionAmount());
        return forexEntity;
    }
}
