package com.thecontainerd.apisnmpprinter.services;

import com.thecontainerd.apisnmpprinter.entities.Printer;
import com.thecontainerd.apisnmpprinter.repositories.PrinterRepository;
import com.thecontainerd.apisnmpprinter.repositories.SNMPManagerRepository;
import com.thecontainerd.apisnmpprinter.services.exceptions.DatabaseException;
import com.thecontainerd.apisnmpprinter.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@EnableScheduling
public class PrinterService {

    @Autowired
    private PrinterRepository repository;

    @Autowired
    private SNMPManagerRepository snmpRepository;

    public List<Printer> findAll() {
        return repository.findAll();
    }

    public Printer findById(Long id) {
        Optional<Printer> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Printer insert(Printer obj) {
        return repository.save(obj);
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public Printer update(Long id, Printer obj) {
        try {
            Printer entity = repository.getOne(id);
            updateData(entity, obj);
            return repository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Printer entity, Printer obj) {
        entity.setCounter(obj.getCounter());
        entity.setIp(obj.getIp());
        entity.setLocalization(obj.getLocalization());
    }

    public List<Printer> findAllSortedBySerieNumber() {
        return repository.findAll(Sort.by("serieNumber").ascending());
    }

    @Scheduled(fixedDelay = 5000)
    private void updatePrinter() {
        System.out.println(snmpRepository.getPrinterCounterV1("10.151.2.151"));
//        printer.setCounter(Long.valueOf(snmpRepository.getPrinterCounterV1(printer.getIp())));
//        printer.setPrinterModel(snmpRepository.getPrinterModelV1(printer.getIp()));
    }

//    @Scheduled(fixedDelay = 10000)
    private void updateAllPrinters() {
        List<Printer> allPrinters = repository.findAll();
        for(Printer p : allPrinters) {
            System.out.println(snmpRepository.getPrinterCounterV1(p.getIp()));
            p.setCounter(Long.valueOf(snmpRepository.getPrinterCounterV1(p.getIp())));
        }
    }
}
