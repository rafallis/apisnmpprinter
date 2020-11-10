package com.thecontainerd.apisnmpprinter.entities;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Entity
@EnableSwagger2
public class Printer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long counter;
    private boolean isOnline;
    private boolean isError;
    private String ip;
    private String serieNumber;
    private String localization;
    private String printerModel;

    public Printer(){}

    public Printer(Long id, Long counter, boolean isOnline, boolean isError, String ip, String serieNumber, String localization, String printerModel) {
        this.id = id;
        this.counter = counter;
        this.isOnline = isOnline;
        this.isError = isError;
        this.ip = ip;
        this.serieNumber = serieNumber;
        this.localization = localization;
        this.printerModel = printerModel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCounter() {
        return counter;
    }

    public void setCounter(Long counter) {
        this.counter = counter;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSerieNumber() {
        return serieNumber;
    }

    public void setSerieNumber(String serieNumber) {
        this.serieNumber = serieNumber;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public String getPrinterModel() {
        return printerModel;
    }

    public void setPrinterModel(String printerModel) {
        this.printerModel = printerModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Printer printer = (Printer) o;
        return id.equals(printer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
