package com.thecontainerd.apisnmpprinter.resources;

import com.thecontainerd.apisnmpprinter.entities.Printer;
import com.thecontainerd.apisnmpprinter.repositories.PrinterPdfExporterRepository;
import com.thecontainerd.apisnmpprinter.services.PrinterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/printers")
@Api(value = "Printers")
public class PrinterResource {

    @Autowired
    private PrinterService service;

    @ApiOperation(value = "Return a list of all printers")
    @GetMapping
    public ResponseEntity<List<Printer>> findAll() {
        List<Printer> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @ApiOperation(value = "Returns a printer object by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Printer> findById(@PathVariable  Long id) {
        Printer obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @ApiOperation(value = "Add printer to printer database")
    @PostMapping
    public ResponseEntity<Printer> insert(@RequestBody Printer obj) {
        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().
                path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @ApiOperation(value = "Delete printer from database")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Update printer object by id")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Printer> update(@PathVariable Long id, @RequestBody Printer obj) {
        obj = service.update(id, obj);
        return ResponseEntity.ok().body(obj);
    }

    @ApiOperation(value = "Export printers report")
    @GetMapping("/report")
    public void exportToPdf(HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=printers_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Printer> listPrinters = service.findAllSortedBySerieNumber();

        PrinterPdfExporterRepository exporter = new com.thecontainerd.apisnmpprinter.repositories.impl.PrinterPdfExporterRepository(listPrinters);
        exporter.export(response);
    }
}
