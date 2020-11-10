package com.thecontainerd.apisnmpprinter.services;

import com.thecontainerd.apisnmpprinter.entities.Printer;
import com.thecontainerd.apisnmpprinter.repositories.PrinterRepository;
import com.thecontainerd.apisnmpprinter.repositories.SNMPManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@EnableScheduling
public class SNMPManagerService {

    @Autowired
    SNMPManagerRepository SNMPRepository;

    @Autowired
    PrinterRepository printerRepository;

    @Scheduled(fixedDelay = 1)
    public void updateAll() {
        List<Printer> printers = printerRepository.findAll();
        for(Printer printer : printers) {

        }
    }
}
