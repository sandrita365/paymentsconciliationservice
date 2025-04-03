package com.payments.paymentsconciliationservice.domain.schemas.back;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class Segment {

    @JacksonXmlProperty(isAttribute = true, localName = "id")
    private String id;
    @JacksonXmlProperty(isAttribute = true, localName = "occ")
    private String occ;

    @JacksonXmlProperty(localName = "CD.ACCNAME")
    @NotNull(message = "The cdAccName field can't be null.")
    @Size(max = 35, message = "The cdAccName field exceeds the maximum size.")
    private String cdAccName;

    @JacksonXmlProperty(localName = "CD.ORDID")
    @NotNull(message = "The cdOrdId field can't be null.")
    @Size(max = 35, message = "The cdOrdId field exceeds the maximum size.")
    private String cdOrdId;

    @JacksonXmlProperty(localName = "CD.ORDNAME")
    @NotNull(message = "The cdOrdName field can't be null.")
    @Size(max = 105, message = "The cdOrdName field exceeds the maximum size.")
    private String cdOrdName;

    @JacksonXmlProperty(localName = "CD.ORDADDRESS")
    @NotNull(message = "The cdOrdAddress field can't be null.")
    @Size(max = 315, message = "The cdOrdAddress field exceeds the maximum size.")
    private String cdOrdAddress;

    @JacksonXmlProperty(localName = "CD.ORDADDRESS1")
    @NotNull(message = "The cdOrdAddress1 field can't be null.")
    @Size(max = 105, message = "The cdOrdAddress1 field exceeds the maximum size.")
    private String cdOrdAddress1;

    @JacksonXmlProperty(localName = "CD.ORDADDRESS2")
    @NotNull(message = "The cdOrdAddress2 field can't be null.")
    @Size(max = 105, message = "The cdOrdAddress2 field exceeds the maximum size.")
    private String cdOrdAddress2;

    @JacksonXmlProperty(localName = "CD.ORDADDRESS3")
    @NotNull(message = "The cdOrdAddress3 field can't be null.")
    @Size(max = 105, message = "The cdOrdAddress3 field exceeds the maximum size.")
    private String cdOrdAddress3;

    @JacksonXmlProperty(localName = "CD.ORDBNKACCNUMER")
    @NotNull(message = "The cdOrdBnkAccNumer field can't be null.")
    @Size(max = 35, message = "The cdOrdBnkAccNumer field exceeds the maximum size.")
    private String cdOrdBnkAccNumer;

    @JacksonXmlProperty(localName = "CD.ORDBNKADDRESS")
    @NotNull(message = "The cdOrdBnkAddress field can't be null.")
    @Size(max = 105, message = "The cdOrdBnkAddress field exceeds the maximum size.")
    private String cdOrdBnkAddress;

    @JacksonXmlProperty(localName = "CD.ORDBNKADDRESS1")
    @NotNull(message = "The cdOrdBnkAddress1 field can't be null.")
    @Size(max = 105, message = "The cdOrdBnkAddress1 field exceeds the maximum size.")
    private String cdOrdBnkAddress1;

    @JacksonXmlProperty(localName = "CD.ORDBNKADDRESS2")
    @NotNull(message = "The cdOrdBnkAddress2 field can't be null.")
    @Size(max = 105, message = "The cdOrdBnkAddress2 field exceeds the maximum size.")
    private String cdOrdBnkAddress2;

    @JacksonXmlProperty(localName = "CD.ORDBNKADDRESS3")
    @NotNull(message = "The cdOrdBnkAddress3 field can't be null.")
    @Size(max = 105, message = "The cdOrdBnkAddress3 field exceeds the maximum size.")
    private String cdOrdBnkAddress3;

    @JacksonXmlProperty(localName = "CD.PAYDET")
    @NotNull(message = "The cdPayDet field can't be null.")
    @Size(max = 255, message = "The cdPayDet field exceeds the maximum size.")
    private String cdPayDet;

    @JacksonXmlProperty(localName = "CD.BNKCLRID")
    @NotNull(message = "The cdBnkClrId field can't be null.")
    @Size(max = 35, message = "The cdBnkClrId field exceeds the maximum size.")
    private String cdBnkClrId;

    @JacksonXmlProperty(localName = "CD.BNKCODTYPE")
    @NotNull(message = "The cdBnkCodType field can't be null.")
    @Size(max = 35, message = "The cdBnkCodType field exceeds the maximum size.")
    private String cdBnkCodType;

    @JacksonXmlProperty(localName = "CD.REMREF")
    @NotNull(message = "The cdRemRef field can't be null.")
    @Size(max = 16, message = "The cdRemRef field exceeds the maximum size.")
    private String cdRemRef;

    @JacksonXmlProperty(localName = "CD.REMDRW")
    @NotNull(message = "The cdRemDrw field can't be null.")
    @Size(max = 16, message = "The cdRemDrw field exceeds the maximum size.")
    private String cdRemDrw;

    @JacksonXmlProperty(localName = "CD.CUSTNUMBER")
    @NotNull(message = "The cdCustNumber field can't be null.")
    @Size(max = 16, message = "The cdCustNumber field exceeds the maximum size.")
    private String cdCustNumber;

    @JacksonXmlProperty(localName = "CD.CUSTREF")
    @NotNull(message = "The cdCustRef field can't be null.")
    @Size(max = 35, message = "The cdCustRef field exceeds the maximum size.")
    private String cdCustRef;


}



