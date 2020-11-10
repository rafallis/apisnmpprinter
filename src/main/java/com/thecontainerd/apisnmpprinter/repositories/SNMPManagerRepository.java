package com.thecontainerd.apisnmpprinter.repositories;

public interface SNMPManagerRepository {

    public String snmpGet(String ipAddress, String community, String oid);

    public String[] snmpWalk(String ipAddress, String community, String oid);

    public String getPrinterLocationV1(String ipAddress);

    public String getPrinterCounterV1(String ipAddress);

    public String getPrinterModelV1(String ipAddress);

    public void testCall();

    /*
    OID List
    .1.3.6.1.2.1
        .6.0                = String: "Location"        *printer registered name
        .4.20.1.1           = IpAddress: 10.151.x.x     *printer ip
        .43.10.2.1.4        = Counter32:765             *print counter
        .43.11.1.1.6.1.1    = String: "TK-1175S"        *toner model
        .43.11.1.1.9.1.1    = INTEGER:12000             *toner level (MAX: 12000)
        .43.5.1.1.16.1      = String: "ECOSYS M2040dn"  *printer model

    USE EXAMPLE

    SNMPManager snmpManager = new SNMPManager();
    snmpManager.snmpGet("10.10.10.10", "public", "1.3.6.1.2.1.1.5.0");
     */
}
