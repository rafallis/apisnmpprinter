package com.thecontainerd.apisnmpprinter.repositories;

import org.dom4j.DocumentException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface PrinterPdfExporterRepository {

    public void export(HttpServletResponse response) throws DocumentException, IOException;
}
