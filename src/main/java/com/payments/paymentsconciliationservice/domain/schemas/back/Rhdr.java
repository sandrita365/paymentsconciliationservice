package com.payments.paymentsconciliationservice.domain.schemas.back;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rhdr {

    @JacksonXmlProperty(isAttribute = true, localName = "id")
    private String id;
    @JacksonXmlProperty(localName = "eid")
    @Size(max = 8, message = "The eid field exceeds the maximum size")
    private String eid;

    @JacksonXmlProperty(localName = "CDCR.CDDR")
    @NotNull(message = "The cdcrCddr field can't be null.")
    @Size(max = 1, message = "The cdcrCddr field exceeds the maximum size.")
    private String cdcrCddr;

    @JacksonXmlProperty(localName = "CD.TRNREF")
    @NotNull(message = "The cdTrnref field can't be null.")
    @Size(max = 16, message = "The cdTrnref field exceeds the maximum size.")
    private String cdTrnref;

    @JacksonXmlProperty(localName = "CD.ACCNUMBER")
    @NotNull(message = "The cdAccnumber field can't be null.")
    @Size(max = 35, message = "The cdAccnumber field exceeds the maximum size.")
    private String cdAccnumber;

    @JacksonXmlProperty(localName = "CD.BNKREF")
    @NotNull(message = "The cdBnkref field can't be null.")
    @Size(max = 16, message = "The cdBnkref field exceeds the maximum size.")
    private String cdBnkref;

    @JacksonXmlProperty(localName = "CD.DATEVAL")
    @Size(max = 8, message = "eid")
    @NotNull(message = "The cdDateval field can't be null.")
    @Size(max = 8, message = "The cdDateval field exceeds the maximum size.")
    private String cdDateval;

    @JacksonXmlProperty(localName = "CD.CCYCR")
    @NotNull(message = "The cdCcycr field can't be null.")
    @Size(max = 3, message = "The cdCcycr field exceeds the maximum size.")
    private String cdCcycr;

    @JacksonXmlProperty(localName = "CD.AMTCR")
    @NotNull(message = "The cdAmtcr field can't be null.")
    @Size(max = 31, message = "The cdAmtcr field exceeds the maximum size.")
    private String cdAmtcr;

    @JacksonXmlProperty(localName = "SRCE.ADDR")
    @NotNull(message = "The srceAddr field can't be null.")
    private String srceAddr;


}
