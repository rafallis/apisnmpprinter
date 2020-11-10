package com.thecontainerd.apisnmpprinter.repositories.impl;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.thecontainerd.apisnmpprinter.entities.Printer;
import org.dom4j.DocumentException;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class PrinterPdfExporterRepository implements com.thecontainerd.apisnmpprinter.repositories.PrinterPdfExporterRepository {

    private List<Printer> listPrinters;

    public PrinterPdfExporterRepository(List<Printer> listPrinters) {
        this.listPrinters = listPrinters;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.white);

        cell.setPhrase(new Phrase("Numero de Serie", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Contador", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Modelo", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        for (Printer printer : listPrinters) {
            table.addCell(String.valueOf(printer.getSerieNumber()));
            String printerCounter = String.valueOf(printer.getCounter());
            table.addCell(printerCounter);
            table.addCell(String.valueOf(printer.getPrinterModel()));
        }
    }

    @Override
    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("Lista de Impressoras", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {3.5f, 3.5f, 3.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);
        document.close();
    }
}
