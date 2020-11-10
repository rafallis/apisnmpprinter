package com.thecontainerd.apisnmpprinter.config;

import com.thecontainerd.apisnmpprinter.entities.Printer;
import com.thecontainerd.apisnmpprinter.repositories.PrinterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private PrinterRepository printerRepository;

    @Override
    public void run(String... args) throws Exception {
        Printer p1 = new Printer(null, 123456L, true, false, "10.10.10.10", "VR123456", "SALA TI", "M2040dn");
        Printer p2 = new Printer(null, 123456L, true, false, "10.10.10.11", "VR789101", "SALA TI 2", "M2040dn");

        printerRepository.saveAll(Arrays.asList(p1, p2));
    }
}
