package com.payments.paymentsconciliationservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.payments.paymentsconciliationservice.domain.model.Forex;
import com.payments.paymentsconciliationservice.domain.schemas.back.ForexGBS;
import com.payments.paymentsconciliationservice.infrastructure.output.MapGBS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class MapGBSTest {
    MapGBS mapGBS;
    private Validator validator;
    @Mock
    private XmlMapper xmlMapper;
    private Forex forex;

    @BeforeEach
    public void setUp() {
        // Inicializar los mocks manualmente
        // Usamos un Validator real solo para la prueba
        MockitoAnnotations.openMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        mapGBS = new MapGBS(validator, xmlMapper);


    }

    private Forex createForex() {
        Forex forex = new Forex();
        forex.setCreditDebitIndicator("C");
        forex.setTransactionReference("1111111111111111");
        forex.setAccountNumber("11111111111111111111111111111111111");
        forex.setAccountName("11111111111111111111111111111111111");
        forex.setBankReference("1111111111111111");
        forex.setValueDate("20250320");
        forex.setCurrency("222");
        forex.setTransactionAmount("2222222222222222222222222222222");
        forex.setOrderOfAccountId("22222222222222222222222222222222222");
        forex.setOrderOfName("222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");
        forex.setOrderOfAddress("222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222211111111111111111111111111111111111111111111111111111111");
        forex.setOrderOfAddress1("111111111111111111111111111111111111111111111112025032322222222222222222222222222222222222222222222222222");
        forex.setOrderOfAddress2("222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");
        forex.setOrderOfAddress3("222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");
        forex.setOrderingBankAccountNumber("22222222222222222222222222222222222");
        forex.setOrderingBankAddress("222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");
        forex.setOrderingBankAddress1("222222222222222222222222222222222111111111111111111111111111111111111111111111111111111111111111111111111");
        forex.setOrderingBankAddress2("111111111111111111111111111111120250204222222222222222222222222222222222222222222222222222222222222222222");
        forex.setOrderingBankAddress3("22222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222x");
        forex.setPaymentDetails("3333333333x222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222x");
        forex.setBankClearingID("22222222222222222222222222222222222");
        forex.setBeneficiaryIDCodeType("22222222222222222222222222222222222");
        forex.setRemitterReference("2222222222222222");
        forex.setRemitterDrawerBillReference("2222222222222222");
        forex.setCustomerNo("2222222222222222");
        forex.setCustomerReference("22222222222222222222222222222222227");

        return forex;
    }

    @Test
    void GBSRecordsValidForexList() {
        forex = createForex();
        List<Forex> forexRecords = List.of(forex);
        List<ForexGBS> result = mapGBS.GBSRecords(forexRecords);

        // Verificar que se ha generado la lista correctamente
        assertNotNull(result);
        assertEquals(1, result.size());

        ForexGBS forexGBS = result.get(0);
        assertNotNull(forexGBS.getShdr());
        assertNotNull(forexGBS.getSegment());

        // Validar algunos valores mapeados
        assertEquals("11111111111111111111111111111111111", forexGBS.getShdr().getCdAccnumber());
        assertEquals("1111111111111111", forexGBS.getShdr().getCdBnkref());
        assertEquals("1111111111111111", forexGBS.getShdr().getCdTrnref());
        assertEquals("11111111111111111111111111111111111", forexGBS.getSegment().getCdAccName());
        assertEquals("222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222", forexGBS.getSegment().getCdOrdName());
    }

    @Test
    void GBSRecordsValidForexFXListCdCdTrnref() {
        forex = createForex();
        forex.setTransactionReference("FX11111111");
        List<Forex> forexRecords = List.of(forex);
        List<ForexGBS> result = mapGBS.GBSRecords(forexRecords);

        // Verificar que se ha generado la lista correctamente
        assertNotNull(result);
        assertEquals(1, result.size());

        ForexGBS forexGBS = result.get(0);
        assertNotNull(forexGBS.getShdr());
        assertNotNull(forexGBS.getSegment());

        // Validar algunos valores mapeados
        assertEquals("11111111111111111111111111111111111", forexGBS.getShdr().getCdAccnumber());
        assertEquals("FX11111111", forexGBS.getShdr().getCdTrnref());
        assertEquals("1111111111111111", forexGBS.getShdr().getCdBnkref());
        assertEquals("11111111111111111111111111111111111", forexGBS.getSegment().getCdAccName());
        assertEquals("222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222", forexGBS.getSegment().getCdOrdName());
    }

    @Test
    void GBSRecordsValidForexRemitterReference() {
        forex = createForex();
        forex.setTransactionReference("FX11111111");
        List<Forex> forexRecords = List.of(forex);
        List<ForexGBS> result = mapGBS.GBSRecords(forexRecords);

        // Verificar que se ha generado la lista correctamente
        assertNotNull(result);
        assertEquals(1, result.size());

        ForexGBS forexGBS = result.get(0);
        assertNotNull(forexGBS.getShdr());
        assertNotNull(forexGBS.getSegment());

        // Validar algunos valores mapeados
        assertEquals("11111111111111111111111111111111111", forexGBS.getShdr().getCdAccnumber());
        assertEquals("FX11111111", forexGBS.getShdr().getCdTrnref());
        assertEquals("1111111111111111", forexGBS.getShdr().getCdBnkref());
        assertEquals("11111111111111111111111111111111111", forexGBS.getSegment().getCdAccName());
        assertEquals("222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222", forexGBS.getSegment().getCdOrdName());
        assertEquals("2222222222222222", forexGBS.getSegment().getCdRemRef());
    }

    @Test
    void GBSRecordsValidForexFXRemitterReference() {
        forex = createForex();
        forex.setRemitterReference("FX22222222");
        List<Forex> forexRecords = List.of(forex);
        List<ForexGBS> result = mapGBS.GBSRecords(forexRecords);

        // Verificar que se ha generado la lista correctamente
        assertNotNull(result);
        assertEquals(1, result.size());

        ForexGBS forexGBS = result.get(0);
        assertNotNull(forexGBS.getShdr());
        assertNotNull(forexGBS.getSegment());

        // Validar algunos valores mapeados
        assertEquals("11111111111111111111111111111111111", forexGBS.getShdr().getCdAccnumber());
        assertEquals("1111111111111111", forexGBS.getShdr().getCdBnkref());
        assertEquals("11111111111111111111111111111111111", forexGBS.getSegment().getCdAccName());
        assertEquals("222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222", forexGBS.getSegment().getCdOrdName());
        assertEquals("FX22222222", forexGBS.getSegment().getCdRemRef());
    }

    @Test
    void shouldThrowExceptionWhenForexRecordsIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> mapGBS.GBSRecords(null));

        assertEquals("forexRecords can't be null or empty.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenForexRecordsIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> mapGBS.GBSRecords(Collections.emptyList()));

        assertEquals("forexRecords can't be null or empty.", exception.getMessage());
    }

    @Test
    void GBSRecordsValidForexNullCUSTREF() {
        Forex forex = createForex();
        forex.setCreditDebitIndicator("1234567890123456789012345678901234567890"); // Campo vacío (debería fallar)
        forex.setAccountNumber(null); // Campo nulo (debería fallar)
        forex.setAccountName(null);
        List<Forex> forexRecords = List.of(forex);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            mapGBS.GBSRecords(forexRecords);
        });
        // Assert: Verifica el mensaje de la excepción
        assertTrue(exception.getMessage().contains("Error de formato ForexGBS"));

    }

    @Test
    void GBSRecordsValidForexMaxValueCUSTREF() {
        Forex forex = createForex();
        forex.setAccountNumber("1234567890123456789012345678901234567890"); // Campo nulo (debería fallar)
        forex.setAccountName("1234567890123456789012345678901234567890");
        List<Forex> forexRecords = List.of(forex);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            mapGBS.GBSRecords(forexRecords);
        });
        // Assert: Verifica el mensaje de la excepción

        assertTrue(exception.getMessage().contains("Error de formato ForexGBS"), exception.getMessage());

    }

    @Test
    void GBSRecordsValidList() {
        Forex forex1 = createForex();
        forex1.setAccountNumber("1234567890123456789012345678901234567890"); // Demasiados caracteres
        forex1.setAccountName("1234567890123456789012345678901234567890");

        Forex forex2 = createForex();
        forex2.setAccountNumber("1234567890123456789012345678901234567890"); // También inválido
        forex2.setAccountName("1234567890123456789012345678901234567890");

        List<Forex> forexRecords = Arrays.asList(forex1, forex2);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            mapGBS.GBSRecords(forexRecords);
        });

        // Assert: Verifica el mensaje de la excepción
        assertTrue(exception.getMessage().contains("Error de formato ForexGBS"), exception.getMessage());
    }
    //validar que se lance exception cuando el objeto es nulo.
    //Que writeValueAsString sea creado correctamente y convertido en Base 64.
    //Lanzar error al querer crear archivo exml.

    @Test
    void serializeObjectToXmlNull() {
        ForexGBS forexGBS = null;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            mapGBS.serializeObjectToXml(forexGBS);
        });
        // Assert: Verifica el mensaje de la excepción
        assertEquals("forexGBS can't be null.", exception.getMessage());
    }

    @Test
    void serializeObjectToXmlOK() {

        Forex forex = createForex();

        List<Forex> forexRecords = List.of(forex);
        List<ForexGBS> GBSList = mapGBS.GBSRecords(forexRecords);
        assertFalse(GBSList.isEmpty(), "GBSList debería contener elementos");
        GBSList.forEach(GBS -> {
            assertDoesNotThrow(() -> mapGBS.serializeObjectToXml(GBS));
        });

    }

    @Test
    void serializeObjectToXmlError() throws JsonProcessingException {
        when(xmlMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Simulated serialization error") {
        });

        ForexGBS forexGBS = new ForexGBS();
        Exception exception = assertThrows(RuntimeException.class, () -> mapGBS.serializeObjectToXml(forexGBS));
        verify(xmlMapper, times(1)).writeValueAsString(any());


    }

}
