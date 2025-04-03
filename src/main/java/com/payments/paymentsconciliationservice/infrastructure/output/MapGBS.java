package com.payments.paymentsconciliationservice.infrastructure.output;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.payments.paymentsconciliationservice.domain.model.Forex;
import com.payments.paymentsconciliationservice.domain.schemas.back.ForexGBS;
import com.payments.paymentsconciliationservice.domain.schemas.back.Rhdr;
import com.payments.paymentsconciliationservice.domain.schemas.back.Segment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MapGBS {
    private final Validator validator;
    private final XmlMapper xmlMapper;


    public MapGBS(Validator validator, XmlMapper xmlMapper) {
        this.validator = validator;
        this.xmlMapper = xmlMapper;
    }

    public List<ForexGBS> GBSRecords(List<Forex> forexRecords) {
        LocalDateTime dateCurrent = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String dateCurrentFormat = dateCurrent.format(formatter);
        log.info("forexRecords size." + forexRecords.size());
        if (forexRecords == null || forexRecords.isEmpty()) {
            log.error("forexRecords can't be null or empty.");
            throw new IllegalArgumentException("forexRecords can't be null or empty.");
        }

        List<ForexGBS> forexGBSList = forexRecords.stream().map(forex -> {

                    ForexGBS forexGBS = new ForexGBS();

            String srceAddr = "A@" +
                    forex.getAccountNumber() +
                    "@" +
                    dateCurrentFormat +
                    "@" +
                    "PaymentsConcilliation@" +
                    forex.getBankReference() +
                    "@" +
                    forex.getTransactionReference() +
                    "@";

                    Rhdr rhdr = new Rhdr("CONFCRED", "DIRECT", forex.getCreditDebitIndicator(),
                            forex.getTransactionReference().startsWith("FX") ? forex.getTransactionReference().substring(0, 10) : forex.getTransactionReference(),
                            forex.getAccountNumber(), forex.getBankReference(),
                            forex.getValueDate(), forex.getCurrency(), forex.getTransactionAmount(), srceAddr

                    );

                    Segment segment = new Segment("XCFCR1CD", "1", forex.getAccountName(), forex.getOrderOfAccountId(), forex.getOrderOfName(), forex.getOrderOfAddress(), forex.getOrderOfAddress1(), forex.getOrderOfAddress2(),
                            forex.getOrderOfAddress3(), forex.getOrderingBankAccountNumber(), forex.getOrderingBankAddress(), forex.getOrderingBankAddress1(), forex.getOrderingBankAddress2(), forex.getOrderingBankAddress3(),
                            forex.getPaymentDetails(), forex.getBankClearingID(), forex.getBeneficiaryIDCodeType(),
                            forex.getRemitterReference().startsWith("FX") ? forex.getRemitterReference().substring(0, 10) : forex.getRemitterReference(),
                            forex.getRemitterDrawerBillReference(), forex.getCustomerNo(), forex.getCustomerReference()

                    );
                    forexGBS.setShdr(rhdr);
                    forexGBS.setSegment(segment);
                    Set<ConstraintViolation<ForexGBS>> result = validator.validate(forexGBS);

                    if (!result.isEmpty()) {

                        String errores = result.stream()
                                .map(v -> v.getPropertyPath() + " - " + v.getMessage())
                                .collect(Collectors.joining(", "));
                        log.error("Error de formato ForexGBS: " + errores);
                        throw new RuntimeException("Error de formato ForexGBS: " + errores);

                    }
                    return forexGBS;
                }
        ).collect(Collectors.toList());
        return forexGBSList;


    }

    public String serializeObjectToXml(ForexGBS forexGBS) {
        if (forexGBS == null) {
            log.error("forexGBS can't be null.");
            throw new IllegalArgumentException("forexGBS can't be null.");
        }
        try {
            // Serialize the Person object to XML
            String xmlForex = xmlMapper.writeValueAsString(forexGBS).replace("&lf", "").replace("&tab", "");
            return Base64.getEncoder().encodeToString(xmlForex.getBytes());

        } catch (JsonProcessingException e) {
            // Handle serialization error for the specific object
            e.printStackTrace();
            throw new RuntimeException("Error serializing object: " + forexGBS, e);
        }
    }
}
