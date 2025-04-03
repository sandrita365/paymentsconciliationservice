package com.payments.paymentsconciliationservice.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Forex {
    private String CreditDebitIndicator;
    private String TransactionReference;
    private String AccountNumber;
    private String AccountName;
    private String BankReference;
    private String ValueDate;
    private String Currency;
    private String TransactionAmount;
    private String OrderOfAccountId;
    private String OrderOfName;
    private String OrderOfAddress;
    private String OrderOfAddress1;
    private String OrderOfAddress2;
    private String OrderOfAddress3;
    private String OrderingBankAccountNumber;
    private String OrderingBankAddress;
    private String OrderingBankAddress1;
    private String OrderingBankAddress2;
    private String OrderingBankAddress3;
    private String PaymentDetails;
    private String BankClearingID;
    private String BeneficiaryIDCodeType;
    private String RemitterReference;
    private String RemitterDrawerBillReference;
    private String CustomerNo;
    private String CustomerReference;

    // Asegura que se incluyan todos los campos relevantes
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Forex forex = (Forex) o;
        return Objects.equals(CreditDebitIndicator, forex.CreditDebitIndicator) &&
                Objects.equals(TransactionReference, forex.TransactionReference) &&
                Objects.equals(AccountNumber, forex.AccountNumber) &&
                Objects.equals(AccountName, forex.AccountName) &&
                Objects.equals(BankReference, forex.BankReference) &&
                Objects.equals(ValueDate, forex.ValueDate) &&
                Objects.equals(Currency, forex.Currency) &&
                Objects.equals(TransactionAmount, forex.TransactionAmount) &&
                Objects.equals(OrderOfAccountId, forex.OrderOfAccountId) &&
                Objects.equals(OrderOfName, forex.OrderOfName) &&
                Objects.equals(OrderOfAddress, forex.OrderOfAddress) &&
                Objects.equals(OrderOfAddress1, forex.OrderOfAddress1) &&
                Objects.equals(OrderOfAddress2, forex.OrderOfAddress2) &&
                Objects.equals(OrderOfAddress3, forex.OrderOfAddress3) &&
                Objects.equals(OrderingBankAccountNumber, forex.OrderingBankAccountNumber) &&
                Objects.equals(OrderingBankAddress, forex.OrderingBankAddress) &&
                Objects.equals(OrderingBankAddress1, forex.OrderingBankAddress1) &&
                Objects.equals(OrderingBankAddress2, forex.OrderingBankAddress2) &&
                Objects.equals(OrderingBankAddress3, forex.OrderingBankAddress3) &&
                Objects.equals(PaymentDetails, forex.PaymentDetails) &&
                Objects.equals(BankClearingID, forex.BankClearingID) &&
                Objects.equals(BeneficiaryIDCodeType, forex.BeneficiaryIDCodeType) &&
                Objects.equals(RemitterReference, forex.RemitterReference) &&
                Objects.equals(RemitterDrawerBillReference, forex.RemitterDrawerBillReference) &&
                Objects.equals(CustomerNo, forex.CustomerNo) &&
                Objects.equals(CustomerReference, forex.CustomerReference);
    }

}
