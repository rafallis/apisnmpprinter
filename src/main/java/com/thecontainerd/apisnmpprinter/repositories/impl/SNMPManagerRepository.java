package com.thecontainerd.apisnmpprinter.repositories.impl;


import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.stereotype.Repository;
import snmp.SNMPObject;
import snmp.SNMPSequence;
import snmp.SNMPVarBindList;
import snmp.SNMPv1CommunicationInterface;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

@Repository
public class SNMPManagerRepository implements com.thecontainerd.apisnmpprinter.repositories.SNMPManagerRepository {

    private static int version = 0;
    private static String protocol = "udp";
    private static String port = "161";

    @Override
    public String snmpGet(String ipAddress, String community, String oid) {
        String resultStat = null;
        StringBuilder address = new StringBuilder();
        address.append(protocol);
        address.append(":");
        address.append(ipAddress);
        address.append("/");
        address.append(port);
        Address targetAddress = GenericAddress.parse(address.toString());
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oid)));
        pdu.setType(PDU.GET);

        // Create a community object
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(targetAddress);
        target.setVersion(SnmpConstants.version1);
        target.setTimeout(2000);
        target.setRetries(1);

        DefaultUdpTransportMapping udpTransportMapping = null;
        Snmp snmp = null;
        try {
            udpTransportMapping = new DefaultUdpTransportMapping();
            udpTransportMapping.listen();
            snmp = new Snmp(udpTransportMapping);
            ResponseEvent response = snmp.send(pdu, target);
            PDU responsePdu = response.getResponse();

            if(responsePdu == null){
                System.out.println(ipAddress+":Request time out");
            }else {
                Object obj = responsePdu.getVariableBindings().firstElement();
                VariableBinding variable = (VariableBinding)obj;
                System.out.println("Variable OID -> " + variable.getOid());
                System.out.println("Variable Value -> " + variable.getVariable());
                resultStat = variable.getVariable().toString();
            }
        }catch (Exception e) {
            System.out.println("An error occurred while getting the SNMP node status" +e);
        }finally{
            if(snmp != null){
                try {
                    snmp.close();
                } catch (IOException e) {
                    snmp = null;
                }
            }
            if(udpTransportMapping != null){
                try {
                    udpTransportMapping.close();
                } catch (Exception e2) {
                    udpTransportMapping = null;
                }
            }
        }
        System.out.println("IP" + ipAddress + " resultStat:"+resultStat);
        return resultStat;
    }

    @Override
    public String[] snmpWalk(String ipAddress, String community, String oid) {
        SNMPv1CommunicationInterface comInterface = null;
        String[] returnValueStrings = null;
        try {
            InetAddress hostAddress = InetAddress.getByName(ipAddress);
            comInterface = new SNMPv1CommunicationInterface(version, hostAddress, community);
            comInterface.setSocketTimeout(2000);

            // Get all management database variable values starting with oid
            SNMPVarBindList tableVars = comInterface.retrieveMIBTable(oid);
            returnValueStrings = new String[tableVars.size()];

            // Loop processing the return value of all nodes starting with oid
            for (int i = 0; i < tableVars.size(); i++) {
                SNMPSequence pair = (SNMPSequence) tableVars.getSNMPObjectAt(i);
                SNMPObject snmpValue = pair.getSNMPObjectAt(i);
                String typesString = snmpValue.getClass().getName();
                if (typesString.equals("snmp.SNMPOctecString")) {
                    String snmpString = snmpValue.toString();
                    int nullLocation = snmpString.indexOf('\0');
                    if (nullLocation >= 0) {
                        snmpString = snmpString.substring(0, nullLocation);
                    }
                    returnValueStrings[i] = snmpString;
                    System.out.println(snmpString);
                } else {
                    returnValueStrings[i] = snmpValue.toString();
                    System.out.print(snmpValue.toString());
                }
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Visit IP is " + ipAddress + ", timeout whtn OID is " + oid + "!");
        } catch (Exception e) {
            System.out.println("An error ocurred while SNMP visited the node");
        } finally {
            if (comInterface != null) {
                try {
                    comInterface.closeConnection();
                } catch (SocketException e) {
                    comInterface = null;
                }
            }
        }
        return returnValueStrings;
    }

    @Override
    public String getPrinterLocationV1(String ipAddress) {
        return snmpGet(ipAddress, "public", ".1.3.6.1.2.1.6.0");
    }

    @Override
    public String getPrinterCounterV1(String ipAddress) {
        return snmpGet(ipAddress, "public", ".1.3.6.1.2.1.43.10.2.1.4");
    }

    @Override
    public String getPrinterModelV1(String ipAddress) {
        return snmpGet(ipAddress, "public", ".1.3.6.1.2.1.43.5.1.1.16.1");
    }

    @Override
    public void testCall() {
        System.out.println("TESTE CHAMADA DO COUNTER: " + getPrinterCounterV1("10.151.2.69"));
    }
}
