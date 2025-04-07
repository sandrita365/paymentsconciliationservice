package com.payments.paymentsconciliationservice.domain;

import com.payments.paymentsconciliationservice.domain.model.Forex;
import com.payments.paymentsconciliationservice.infrastructure.config.ForexRecordFieldPositions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class ForexProcesor {

    public static Forex parseLine(String line) {
        log.info("In parseLine methodo :::" + line.length());
        if (line.length() < ForexRecordFieldPositions.SIZE_LINE) {
            throw new IllegalArgumentException("String is too short for expected fields.");
        }
        String CreditDebitIndicator = line.substring(ForexRecordFieldPositions.CREDIT_DEBIT_INDICATOR_START, ForexRecordFieldPositions.CREDIT_DEBIT_INDICATOR_END);

        String TransactionReference = line.substring(ForexRecordFieldPositions.TRANSACTION_REFERENCE_START, ForexRecordFieldPositions.TRANSACTION_REFERENCE_END);
        String AccountNumber = line.substring(ForexRecordFieldPositions.ACCOUNT_NUMBER_START, ForexRecordFieldPositions.ACCOUNT_NUMBER_END);
        String AccountName = line.substring(ForexRecordFieldPositions.ACCOUNT_NAME_START, ForexRecordFieldPositions.ACCOUNT_NAME_END);
        String BankReference = line.substring(ForexRecordFieldPositions.BANK_REFERENCE_START, ForexRecordFieldPositions.BANK_REFERENCE_END);
        String ValueDate = line.substring(ForexRecordFieldPositions.VALUE_DATE_START, ForexRecordFieldPositions.VALUE_DATE_END);
        String Currency = line.substring(ForexRecordFieldPositions.CURRENCY_START, ForexRecordFieldPositions.CURRENCY_END);
        String TransactionAmount = line.substring(ForexRecordFieldPositions.TRANSACTION_AMOUNT_START, ForexRecordFieldPositions.TRANSACTION_AMOUNT_END);
        String OrderOfAccountId = line.substring(ForexRecordFieldPositions.ORDER_OF_ACCOUNT_ID_START, ForexRecordFieldPositions.ORDER_OF_ACCOUNT_ID_END);
        String OrderOfName = line.substring(ForexRecordFieldPositions.ORDER_OF_NAME_START, ForexRecordFieldPositions.ORDER_OF_NAME_END);
        String OrderOfAddress = line.substring(ForexRecordFieldPositions.ORDER_OF_ADDRESS_START, ForexRecordFieldPositions.ORDER_OF_ADDRESS_END);
        String OrderOfAddress1 = line.substring(ForexRecordFieldPositions.ORDER_OF_ADDRESS1_START, ForexRecordFieldPositions.ORDER_OF_ADDRESS1_END);
        String OrderOfAddress2 = line.substring(ForexRecordFieldPositions.ORDER_OF_ADDRESS2_START, ForexRecordFieldPositions.ORDER_OF_ADDRESS2_END);
        String OrderOfAddress3 = line.substring(ForexRecordFieldPositions.ORDER_OF_ADDRESS3_START, ForexRecordFieldPositions.ORDER_OF_ADDRESS3_END);
        String OrderingBankAccountNumber = line.substring(ForexRecordFieldPositions.ORDERING_BANK_ACCOUNT_NUMBER_START, ForexRecordFieldPositions.ORDERING_BANK_ACCOUNT_NUMBER_END);
        String OrderingBankAddress = line.substring(ForexRecordFieldPositions.ORDERING_BANK_ADDRESS_START, ForexRecordFieldPositions.ORDERING_BANK_ADDRESS_END);
        String OrderingBankAddress1 = line.substring(ForexRecordFieldPositions.ORDERING_BANK_ADDRESS1_START, ForexRecordFieldPositions.ORDERING_BANK_ADDRESS1_END);
        String OrderingBankAddress2 = line.substring(ForexRecordFieldPositions.ORDERING_BANK_ADDRESS2_START, ForexRecordFieldPositions.ORDERING_BANK_ADDRESS2_END);
        String OrderingBankAddress3 = line.substring(ForexRecordFieldPositions.ORDERING_BANK_ADDRESS3_START, ForexRecordFieldPositions.ORDERING_BANK_ADDRESS3_END);
        String PaymentDetails = line.substring(ForexRecordFieldPositions.PAYMENT_DETAILS_START, ForexRecordFieldPositions.PAYMENT_DETAILS_END);
        String BankClearingId = line.substring(ForexRecordFieldPositions.BANK_CLEARING_ID_START, ForexRecordFieldPositions.BANK_CLEARING_ID_END);
        String BeneficiaryIdCodeType = line.substring(ForexRecordFieldPositions.BENEFICIARY_ID_CODE_TYPE_START, ForexRecordFieldPositions.BENEFICIARY_ID_CODE_TYPE_END);
        String RemitterReference = line.substring(ForexRecordFieldPositions.REMITTER_REFERENCE_START, ForexRecordFieldPositions.REMITTER_REFERENCE_END);
        String RemitterDrawerBillReference = line.substring(ForexRecordFieldPositions.REMITTER_DRAWER_BILL_REFERENCE_START, ForexRecordFieldPositions.REMITTER_DRAWER_BILL_REFERENCE_END);
        String CustomerNo = line.substring(ForexRecordFieldPositions.CUSTOMER_NO_START, ForexRecordFieldPositions.CUSTOMER_NO_END);
        String CustomerReference = line.substring(ForexRecordFieldPositions.CUSTOMER_REFERENCE_START, ForexRecordFieldPositions.CUSTOMER_REFERENCE_END);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate today = LocalDate.now();
        try {
            // Intenta parsear la cadena al formato deseado
            LocalDate date = LocalDate.parse(ValueDate, formatter);
        } catch (DateTimeParseException e) {
            // Lanza una excepción personalizada si el formato es incorrecto
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyyMMdd");
        }

        log.info("Out parseLine method :::");
        return new Forex(CreditDebitIndicator, TransactionReference, AccountNumber, AccountName, BankReference,
                ValueDate, Currency, TransactionAmount, OrderOfAccountId, OrderOfName, OrderOfAddress,
                OrderOfAddress1, OrderOfAddress2, OrderOfAddress3, OrderingBankAccountNumber,
                OrderingBankAddress, OrderingBankAddress1, OrderingBankAddress2, OrderingBankAddress3,
                PaymentDetails, BankClearingId, BeneficiaryIdCodeType, RemitterReference,
                RemitterDrawerBillReference, CustomerNo, CustomerReference);


    }

    public List<Forex> filterRecords(List<Forex> forexList) {
        log.info("In filterRecords method :::");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate today = LocalDate.now();
        if (forexList == null || forexList.isEmpty()) {
            log.error("An error occurred: The object cannot be null or empty.");
            throw new IllegalArgumentException("The object cannot be null or empty.");

        }
        List<Forex> filteredForexList = forexList.stream().
                filter(forex -> (
                        !forex.getCreditDebitIndicator().equals("D") && (LocalDate.parse(forex.getValueDate(), formatter).equals(today))
                )).peek(forex ->
                        {
                            log.debug("forex.getCreditDebitIndicator() {}", forex.getCreditDebitIndicator());
                            log.debug("forex.getValueDate() {}", forex.getValueDate());
                        }
                ).collect(Collectors.toList());
        if (filteredForexList.size() == 0) log.info("There is no records to process.");
        log.info("Out filterRecords method :::");
        return filteredForexList;


    }
}
