package com.payments.paymentsconciliationservice.domain.model;

import lombok.Data;

@Data
public class CdInFile {
    private String INDICATOR;
    private String TRANSACTION_REFERENCE;
    private String ACCOUNT_NUMBER;
    private String BANK_REFERENCE;
    private String VALUE_DATE;
    private String CURRENCY;
    private String TRANSACTION_AMOUNT;

}
