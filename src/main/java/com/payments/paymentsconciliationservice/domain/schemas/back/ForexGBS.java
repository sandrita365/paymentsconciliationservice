package com.payments.paymentsconciliationservice.domain.schemas.back;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@JacksonXmlRootElement(localName = "forex", namespace = "http://www.banamex.com/soa/mx/treasury/domain/FX_MM/Request/1.00")
public class ForexGBS {
    @NotNull(message = "Rhdr no puede ser nulo")
    @Valid
    @JacksonXmlProperty(localName = "rHdr", namespace = "http://www.banamex.com/soa/mx/treasury/domain/FX_MM/Request/1.00")
    private Rhdr shdr;

    @NotNull(message = "Segment no puede ser nulo")
    @Valid
    @JacksonXmlProperty(localName = "segment", namespace = "http://www.banamex.com/soa/mx/treasury/domain/FX_MM/Request/1.00")
    private Segment segment;

}

