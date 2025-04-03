package com.payments.paymentsconciliationservice.aplication;

import com.payments.paymentsconciliationservice.domain.ForexProcesor;
import com.payments.paymentsconciliationservice.domain.model.Forex;
import com.payments.paymentsconciliationservice.domain.ports.input.ProcessFileCUPort;
import com.payments.paymentsconciliationservice.domain.ports.output.ForexRepositoryPort;
import com.payments.paymentsconciliationservice.infrastructure.exceptions.CustomProcessException;
import com.payments.paymentsconciliationservice.infrastructure.exceptions.ErrorMessageService;
import com.payments.paymentsconciliationservice.infrastructure.output.MapGBS;
import com.payments.paymentsconciliationservice.infrastructure.output.SendMessageGBS;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProcessFileCUAdapter implements ProcessFileCUPort {

    ForexRepositoryPort forexRepositoryPort;
    private final MapGBS mapGBS;
    private final SendMessageGBS sendMessageGBS;
    private final ErrorMessageService errorMessageService;
    private final ForexProcesor forexProcesor;

    private final String destinationQueue;
    private final String DIRECTORY_TO_MONITOR;
    private final String FILE_TO_MONITOR;
    private final long POLL_INTERVAL;
    private FileAlterationMonitor monitor;

    @Autowired
    public ProcessFileCUAdapter(
            ForexRepositoryPort forexRepositoryPort,
            MapGBS mapGBS,
            SendMessageGBS sendMessageGBS,
            ErrorMessageService errorMessageService,
            ForexProcesor forexProcesor,
            @Value("${app.connections.ems.destinationnames.queue}") String destination,
            @Value("${app.intellar.filepaths.rcve}") String directoryToMonitor,
            @Value("${app.intellar.filepaths.filename}") String fileToMonitor,
            @Value("${app.intellar.polling.interval.secs}") long pollInterval
    ) {
        this.forexRepositoryPort = forexRepositoryPort;
        this.mapGBS = mapGBS;
        this.sendMessageGBS = sendMessageGBS;
        this.errorMessageService = errorMessageService;
        this.forexProcesor = forexProcesor;
        this.destinationQueue = destination;
        this.DIRECTORY_TO_MONITOR = directoryToMonitor;
        this.FILE_TO_MONITOR = fileToMonitor;
        this.POLL_INTERVAL = pollInterval;

    }

    @Override
    public void startMonitoring() {
        log.info("STARTER::::::.");
        try {
            // Observador para el directorio especificado
            FileAlterationObserver observer = new FileAlterationObserver(DIRECTORY_TO_MONITOR);

            // Agregar un listener para manejar los eventos
            observer.addListener(new FileAlterationListenerAdaptor() {

                @Override
                public void onFileCreate(File file) {
                    log.info("In onFileCreate method:::");
                    if (file.getName().equals(FILE_TO_MONITOR)) {

                        try {
                            procesor(DIRECTORY_TO_MONITOR + "/" + file.getName());
                            log.info("Out onFileCreate method:::");
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (Exception e) {
                            // Handle serialization error for the specific object
                            e.printStackTrace();
                        }
                        System.out.println("Archivo creado: " + DIRECTORY_TO_MONITOR + "/" + file.getName());
                    }
                }

                @Override
                public void onFileDelete(File file) {
                    System.out.println("Archivo onFileDelete: " + file.getName());
                    if (file.getName().equals(FILE_TO_MONITOR)) {
                        System.out.println("Archivo eliminado: " + file.getName());
                    }
                }

                @Override
                public void onFileChange(File file) {
                    log.info("In onFileChange method:::");
                    if (file.getName().equals(FILE_TO_MONITOR)) {
                        System.out.println("Archivo modificado: " + file.getPath());
                        try {
                            procesor(DIRECTORY_TO_MONITOR + "/" + file.getName());
                        } catch (FileNotFoundException e) {
                            // Handle specific exception - no need to wrap it in a RuntimeException
                            log.error("File not found: " + e.getMessage(), e); // Log the error
                        } catch (Exception e) {
                            // Handle any other unexpected exceptions
                            log.error("Unexpected error occurred while processing file: " + e.getMessage(), e);
                        }

                    }
                }

            });

            // Monitor con intervalo de sondeo
            monitor = new FileAlterationMonitor(POLL_INTERVAL, observer);
            log.info("EMonitor con intervalo de sondeo: ");
            // Iniciar el monitoreo
            monitor.start();
            log.info("EMonitor con intervalo de sondeo: ");

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error al iniciar el monitoreo: " + e.getMessage(), e);
        }

    }

    public void stopMonitoring() {
        try {
            if (monitor != null) {
                monitor.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.trace("Error al iniciar el monitoreo: " + e.getMessage());
        }
    }


    public void procesor(String filePath) throws FileNotFoundException {
        log.info("In procesor method: filepath {}:", filePath);
        try {
            List<Forex> listForex = fileReader(filePath);


            if (!listForex.isEmpty()) {
                List<Forex> filterRecords = forexProcesor.filterRecords(listForex);
                filterRecords.forEach(forex -> {

                    forexRepositoryPort.guardar(forex);
                    log.info("GUARDAR:::");
                });
                mapGBS.GBSRecords(filterRecords).forEach(obj -> {
                    try {
                        String base64EncodedXml = mapGBS.serializeObjectToXml(obj);
                        sendMessageGBS.MessageSender(destinationQueue, base64EncodedXml);
                    } catch (Exception e) {
                        // Handle serialization error for the specific object
                        e.printStackTrace();
                        log.error("Error:: ", e.getMessage());
                        throw new RuntimeException("Error:: " + obj.toString(), e);
                    }

                });

            } else {
                log.warn(new CustomProcessException("NO_RECORDS_TO_PROCESS", errorMessageService).getMessage());
            }
        } catch (Exception e) {
            // Handle serialization error for the specific object
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RuntimeException("Error to process the file ", e);
        }


    }


    public List<Forex> fileReader(String filePath) throws FileNotFoundException {
        log.info("In FileReader method :::");
        List<Forex> records = new ArrayList<>();
        // Files.lines(Paths.get(filePath));

        // Case 1: File.lines devuelve el contenido
        // Case 2: Cualquier parte del código genere IllegalArgumentException
        // when(object.method()).thenThrows(IOException.class);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {

                records.add(ForexProcesor.parseLine(line));
            }
            log.info("Out FileReader method :::");
        } catch (IllegalArgumentException e) {
            log.error("Invalid input: {}", e.getMessage());
            // You can throw a custom exception or return a default value
            throw new CustomProcessException("INVALID_INPUT", errorMessageService);
        } catch (IOException e) {
            log.trace("An error occurred while processing the file: {}", e.getMessage());
        }
        return records;
    }

}
