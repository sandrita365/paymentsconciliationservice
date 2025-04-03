package com.payments.paymentsconciliationservice;

import com.payments.paymentsconciliationservice.aplication.ProcessFileCUAdapter;
import com.payments.paymentsconciliationservice.domain.ForexProcesor;
import com.payments.paymentsconciliationservice.domain.model.Forex;
import com.payments.paymentsconciliationservice.domain.ports.output.ForexRepositoryPort;
import com.payments.paymentsconciliationservice.domain.schemas.back.ForexGBS;
import com.payments.paymentsconciliationservice.infrastructure.exceptions.ErrorMessageService;
import com.payments.paymentsconciliationservice.infrastructure.output.MapGBS;
import com.payments.paymentsconciliationservice.infrastructure.output.SendMessageGBS;
import com.payments.paymentsconciliationservice.infrastructure.persistencie.entity.ForexEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ProcessFileCUAdapterTest {
    private ProcessFileCUAdapter processFileCUAdapter;

    @Mock
    private ForexRepositoryPort forexRepositoryPort;

    @Mock
    private MapGBS mapGBS;

    @Mock
    private SendMessageGBS sendMessageGBS;

    @Mock
    private ErrorMessageService errorMessageService;

    @Mock
    private Logger log;
    @Mock
    private Forex forex;

    @Mock
    ForexProcesor forexProcesor;

    private String destinationQueue;
    private String DIRECTORY_TO_MONITOR;
    private String FILE_TO_MONITOR;
    private long POLL_INTERVAL;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        destinationQueue = "";
        DIRECTORY_TO_MONITOR = "";
        FILE_TO_MONITOR = "";
        POLL_INTERVAL = 5000;
        processFileCUAdapter = spy(new ProcessFileCUAdapter(forexRepositoryPort, mapGBS, sendMessageGBS, errorMessageService, forexProcesor,
                destinationQueue, DIRECTORY_TO_MONITOR, FILE_TO_MONITOR, POLL_INTERVAL));
    }

    @Test
    void testProcesor_WhenNoRecordsToProcess() throws FileNotFoundException {
        String filePath = "C:/res/test/DUMMY_CDINFILE";
        Forex Forex = null;

        doReturn(Collections.emptyList()).when(processFileCUAdapter).fileReader(anyString());


        processFileCUAdapter.procesor(filePath);


        verify(forexRepositoryPort, never()).guardar(any());
        verify(sendMessageGBS, never()).MessageSender(any(), any());
    }

    @Test
    void testProcesor_ok() throws FileNotFoundException {
        String filePath = "C:/res/test/DUMMY_CDINFILE";
        Forex forex1 = createForex();
        Forex forex2 = createForex();
        Forex forex3 = createForex();

        List<Forex> ListForexMock = Arrays.asList(forex1, forex2, forex3);

        List<Forex> FilterListForexMock = Arrays.asList(forex1, forex2);

        List<ForexGBS> listGBS = Arrays.asList(new ForexGBS(), new ForexGBS());

        doReturn(ListForexMock).when(processFileCUAdapter).fileReader(anyString());
        doReturn(FilterListForexMock).when(forexProcesor).filterRecords(argThat(list -> list != null && !list.isEmpty()));
        when(forexRepositoryPort.guardar(any(Forex.class))).thenReturn(new ForexEntity());
        when(mapGBS.GBSRecords(FilterListForexMock)).thenReturn(listGBS);
        when(mapGBS.serializeObjectToXml(any(ForexGBS.class))).thenReturn("<xml><forex>Mocked XML</forex></xml>");
        doNothing().when(sendMessageGBS).MessageSender(anyString(), anyString());

        processFileCUAdapter.procesor(filePath);
        verify(forexRepositoryPort, times(FilterListForexMock.size())).guardar(any(Forex.class));
        verify(mapGBS, times(FilterListForexMock.size())).serializeObjectToXml(any(ForexGBS.class));
        verify(sendMessageGBS, times(FilterListForexMock.size())).MessageSender(anyString(), anyString());
    }

    @Test
    void testProcesor_Error_serializeObjectToXml() throws FileNotFoundException {
        String filePath = "C:/res/test/DUMMY_CDINFILE";
        Forex forex1 = createForex();
        Forex forex2 = createForex();
        Forex forex3 = createForex();

        List<Forex> ListForexMock = Arrays.asList(forex1, forex2, forex3);

        List<Forex> FilterListForexMock = Arrays.asList(forex1, forex2);

        List<ForexGBS> listGBS = Arrays.asList(new ForexGBS(), new ForexGBS());

        doReturn(ListForexMock).when(processFileCUAdapter).fileReader(anyString());
        doReturn(FilterListForexMock).when(forexProcesor).filterRecords(argThat(list -> list != null && !list.isEmpty()));
        when(forexRepositoryPort.guardar(any(Forex.class))).thenReturn(new ForexEntity());
        when(mapGBS.GBSRecords(FilterListForexMock)).thenReturn(listGBS);
        doThrow(new RuntimeException("Error"))
                .when(mapGBS).serializeObjectToXml(any());


        Exception exception = assertThrows(RuntimeException.class, () -> {
            processFileCUAdapter.procesor(filePath);
        });

        verify(forexRepositoryPort, times(FilterListForexMock.size())).guardar(any(Forex.class));
        assertTrue(exception.getMessage().contains("Error"), exception.getMessage());
        verify(sendMessageGBS, never()).MessageSender(anyString(), anyString());
    }

    @Test
    void testProcesor_Error_fileReader() throws FileNotFoundException {

        doThrow(new RuntimeException("Error to process the file "))
                .when(processFileCUAdapter).fileReader(anyString());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            processFileCUAdapter.procesor(anyString());
        });

        assertTrue(exception.getMessage().contains("Error"), exception.getMessage());
        verify(sendMessageGBS, never()).MessageSender(anyString(), anyString());
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


}
