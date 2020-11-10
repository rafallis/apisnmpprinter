package com.thecontainerd.apisnmpprinter.repositories;

import com.thecontainerd.apisnmpprinter.entities.Printer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrinterRepository extends JpaRepository<Printer, Long> {
}
