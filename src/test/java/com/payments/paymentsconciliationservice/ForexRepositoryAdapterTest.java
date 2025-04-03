package com.payments.paymentsconciliationservice;

import com.payments.paymentsconciliationservice.domain.model.Forex;
import com.payments.paymentsconciliationservice.infrastructure.adapters.output.ForexRepositoryAdapter;
import com.payments.paymentsconciliationservice.infrastructure.persistencie.ForexJpaRepository;
import com.payments.paymentsconciliationservice.infrastructure.persistencie.entity.ForexEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
public class ForexRepositoryAdapterTest {
    //Guardar objeto correctamente.
    //Elviar un objeto nulo y que se lance un nullpointer exception.
    //Repositorio regresa null  o lanza una excepción para verificar que guardar() lo maneja correctamente.
    ForexRepositoryAdapter forexRepositoryAdapter;

    @Mock
    private ForexJpaRepository forexJpaRepository;
    private Forex forex;
    private ForexEntity forexEntity;

    @BeforeEach
    public void setUp() {
        // Inicializar los mocks manualmente
        MockitoAnnotations.openMocks(this);

        forexRepositoryAdapter = new ForexRepositoryAdapter(forexJpaRepository);
        forex = new Forex();
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
        forex.setOrderOfAddress1("111111111111111111111111111111111111111111111112025020422222222222222222222222222222222222222222222222222");
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

        forexEntity = new ForexEntity();
        forexEntity.setIndicator("C");
        forexEntity.setTransactionReference("1111111111111111");
        forexEntity.setAccountNumber("11111111111111111111111111111111111");
        forexEntity.setBankReference("1111111111111111");
        forexEntity.setValueDate("20250320");
        forexEntity.setCurrency("222");
        forexEntity.setTransactionAmount("2222222222222222222222222222222");


    }

    //Checking if an object is saved correctly.
    @Test
    void guardarOKTest() {

        when(forexJpaRepository.save(any(ForexEntity.class))).thenReturn(forexEntity);
        ForexEntity resultForexEntity = forexRepositoryAdapter.guardar(forex);

        verify(forexJpaRepository, times(1)).save(any(ForexEntity.class));
        // Validar que el objeto devuelto tiene los valores correctos
        assertNotNull(resultForexEntity);
        assertEquals("C", resultForexEntity.getIndicator());
        assertEquals("1111111111111111", resultForexEntity.getTransactionReference());
        assertEquals("11111111111111111111111111111111111", resultForexEntity.getAccountNumber());
        assertEquals("1111111111111111", resultForexEntity.getBankReference());
        assertEquals("20250320", resultForexEntity.getValueDate());
        assertEquals("222", resultForexEntity.getCurrency());
        assertEquals("2222222222222222222222222222222", resultForexEntity.getTransactionAmount());


    }

    //Checking that the objetc is not null.
    @Test
    void guardarNullTest() {
        forex = null;
        Exception nullException = assertThrows(RuntimeException.class, () -> {
            forexRepositoryAdapter.guardar(forex);
        });
        // Assert: Verifica el mensaje de la excepción
        assertEquals("Forex can't be null.", nullException.getMessage());


    }

    //Throw an error to save
    @Test
    void guardarErrorTest() {

        forex = new Forex();
        forex.setCreditDebitIndicator("C");
        when(forexJpaRepository.save(any(ForexEntity.class)))
                .thenThrow(new RuntimeException("No field should be null."));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            forexRepositoryAdapter.guardar(forex);
        });

        assertEquals("No field should be null.", exception.getMessage());

    }

    @Test
    void testGuardar_Indicator_MaxLengthError() {
        // Datos válidos
        forex = new Forex();
        forex.setCreditDebitIndicator("C");
        forex.setTransactionReference("TRX12345678901234");
        forex.setAccountNumber("12345678901234567890123456789012345");
        forex.setAccountName("12345678901234567890123456789012345");
        forex.setBankReference("BANKREF1234567890");
        forex.setValueDate("20250322");
        forex.setCurrency("USD");
        forex.setTransactionAmount("10000000000000000000000000000.99");

        // Simular que la base de datos lanza un error
        when(forexJpaRepository.save(any(ForexEntity.class))).thenThrow(new RuntimeException("Indicator must be 1 character max"));

        // Verificar que se lanza una excepción al intentar guardar
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            forexRepositoryAdapter.guardar(forex);
        });

        assertEquals("Indicator must be 1 characters max", exception.getMessage());
        verify(forexJpaRepository, never()).save(any()); // Verifica que no se intentó guardar nada.
    }

    @Test
    void testGuardar_TransactionReference_MaxLengthError() {
        // Datos válidos
        forex = new Forex();
        forex.setCreditDebitIndicator("C");
        forex.setTransactionReference("TRX12345678901234X");
        forex.setAccountNumber("12345678901234567890123456789012345");
        forex.setAccountName("12345678901234567890123456789012345");
        forex.setBankReference("BANKREF123456789");
        forex.setValueDate("20250322");
        forex.setCurrency("USD");
        forex.setTransactionAmount("10000000000000000000000000000.9");

        // Simular que la base de datos lanza un error
        when(forexJpaRepository.save(any(ForexEntity.class))).thenThrow(new RuntimeException("TransactionReference must be 17 character max."));

        // Verificar que se lanza una excepción al intentar guardar
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            forexRepositoryAdapter.guardar(forex);
        });

        assertEquals("TransactionReference must be 17 characters max.", exception.getMessage());
        verify(forexJpaRepository, never()).save(any()); // Verifica que no se intentó guardar nada.
    }

    @Test
    void testGuardar_AccountNumber_MaxLengthError() {
        // Datos válidos
        forex = new Forex();
        forex.setCreditDebitIndicator("C");
        forex.setTransactionReference("TRX12345678901234");
        forex.setAccountNumber("12345678901234567890123456789012345X");
        forex.setAccountName("12345678901234567890123456789012345");
        forex.setBankReference("BANKREF123456789");
        forex.setValueDate("20250322");
        forex.setCurrency("USD");
        forex.setTransactionAmount("10000000000000000000000000000.9");

        // Simular que la base de datos lanza un error
        when(forexJpaRepository.save(any(ForexEntity.class))).thenThrow(new RuntimeException("AccountNumber must be 35 character max."));

        // Verificar que se lanza una excepción al intentar guardar
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            forexRepositoryAdapter.guardar(forex);
        });

        assertEquals("AccountNumber must be 35 characters max.", exception.getMessage());
        verify(forexJpaRepository, never()).save(any()); // Verifica que no se intentó guardar nada.
    }

    @Test
    void testGuardar_BankReference_MaxLengthError() {
        // Datos válidos
        forex = new Forex();
        forex.setCreditDebitIndicator("C");
        forex.setTransactionReference("TRX12345678901234");
        forex.setAccountNumber("12345678901234567890123456789012345");
        forex.setAccountName("12345678901234567890123456789012345");
        forex.setBankReference("BANKREF123456789X");
        forex.setValueDate("20250322");
        forex.setCurrency("USD");
        forex.setTransactionAmount("10000000000000000000000000000.9");

        // Simular que la base de datos lanza un error
        when(forexJpaRepository.save(any(ForexEntity.class))).thenThrow(new RuntimeException("BankReference must be 16 character max."));

        // Verificar que se lanza una excepción al intentar guardar
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            forexRepositoryAdapter.guardar(forex);
        });

        assertEquals("BankReference must be 16 characters max.", exception.getMessage());
        verify(forexJpaRepository, never()).save(any()); // Verifica que no se intentó guardar nada.
    }

    @Test
    void testGuardar_ValueDate_MaxLengthError() {
        // Datos válidos
        forex = new Forex();
        forex.setCreditDebitIndicator("C");
        forex.setTransactionReference("TRX12345678901234");
        forex.setAccountNumber("12345678901234567890123456789012345");
        forex.setAccountName("12345678901234567890123456789012345");
        forex.setBankReference("BANKREF123456789");
        forex.setValueDate("20250322X");
        forex.setCurrency("USD");
        forex.setTransactionAmount("10000000000000000000000000000.9");

        // Simular que la base de datos lanza un error
        when(forexJpaRepository.save(any(ForexEntity.class))).thenThrow(new RuntimeException("ValueDate must be 8 character max."));

        // Verificar que se lanza una excepción al intentar guardar
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            forexRepositoryAdapter.guardar(forex);
        });

        assertEquals("ValueDate must be 8 characters max.", exception.getMessage());
        verify(forexJpaRepository, never()).save(any()); // Verifica que no se intentó guardar nada.
    }

    @Test
    void testGuardar_Currency_MaxLengthError() {
        // Datos válidos
        forex = new Forex();
        forex.setCreditDebitIndicator("C");
        forex.setTransactionReference("TRX12345678901234");
        forex.setAccountNumber("12345678901234567890123456789012345");
        forex.setAccountName("12345678901234567890123456789012345");
        forex.setBankReference("BANKREF123456789");
        forex.setValueDate("20250322");
        forex.setCurrency("USDC");
        forex.setTransactionAmount("10000000000000000000000000000.9");

        // Simular que la base de datos lanza un error
        when(forexJpaRepository.save(any(ForexEntity.class))).thenThrow(new RuntimeException("Currency must be 3 character max."));

        // Verificar que se lanza una excepción al intentar guardar
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            forexRepositoryAdapter.guardar(forex);
        });

        assertEquals("Currency must be 3 character max.", exception.getMessage());
        verify(forexJpaRepository, never()).save(any()); // Verifica que no se intentó guardar nada.
    }

    @Test
    void testGuardar_TransactionAmount_MaxLengthError() {
        // Datos válidos
        forex = new Forex();
        forex.setCreditDebitIndicator("C");
        forex.setTransactionReference("TRX12345678901234");
        forex.setAccountNumber("12345678901234567890123456789012345");
        forex.setAccountName("12345678901234567890123456789012345");
        forex.setBankReference("BANKREF123456789");
        forex.setValueDate("20250322");
        forex.setCurrency("USD");
        forex.setTransactionAmount("10000000000000000000000000000.9X");

        // Simular que la base de datos lanza un error
        when(forexJpaRepository.save(any(ForexEntity.class))).thenThrow(new RuntimeException("TransactionAmount must be 31 character max."));

        // Verificar que se lanza una excepción al intentar guardar
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            forexRepositoryAdapter.guardar(forex);
        });

        assertEquals("TransactionAmount must be 31 characters max.", exception.getMessage());
        verify(forexJpaRepository, never()).save(any()); // Verifica que no se intentó guardar nada.
    }


}
