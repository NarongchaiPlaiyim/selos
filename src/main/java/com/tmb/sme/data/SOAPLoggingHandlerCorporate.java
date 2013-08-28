package com.tmb.sme.data;

import com.clevel.selos.controller.TestRM;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Set;

public class SOAPLoggingHandlerCorporate implements SOAPHandler<SOAPMessageContext>{

    @Override
    public boolean handleMessage(SOAPMessageContext context) {

        System.out.println("Client : handleMessage()......");

        Boolean isRequest = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        //if this is a request, true for outbound messages, false for inbound
        if(isRequest){

            try{
                SOAPMessage soapMsg = context.getMessage();
                SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
                SOAPHeader soapHeader = soapEnv.getHeader();


                //if no header, add one
                if (soapHeader == null){
                    soapHeader = soapEnv.addHeader();
                }

                //get mac address
                String mac = getMACAddress();

                //add a soap header, name as "mac address"
                QName qname = new QName("http://data.sme.tmb.com/EAISearchCorporateCustomer/", "EAISearchCorporateCustomer");
                SOAPHeaderElement soapHeaderElement = soapHeader.addHeaderElement(qname);

                soapHeaderElement.setActor(SOAPConstants.URI_SOAP_ACTOR_NEXT);
                soapHeaderElement.addTextNode(mac);
                soapMsg.saveChanges();

                //tracking
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                soapMsg.writeTo(baos);
                TestRM.printRequest=baos.toString();
                SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
                SOAPConnection soapConnection = soapConnectionFactory.createConnection();

                System.out.println("Response...........................................................");

                // Send SOAP 	Message to SOAP Server
                String url = "http://10.175.140.18:7807/EAISearchCorporateCustomer";
                SOAPMessage soapResponse = soapConnection.call(soapMsg, url);
               soapResponse.writeTo(baos);
                TestRM.printResponse=baos.toString();
            }catch(SOAPException e){
                System.err.println(e);
            }catch(IOException e){
                System.err.println(e);
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }


        //continue other handler chain
        return true;
    }
    private static void printSOAPResponse(SOAPMessage soapResponse) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        System.out.print("\nResponse SOAP Message = ");
        StreamResult result = new StreamResult(System.out);
        transformer.transform(sourceContent, result);
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        System.out.println("Client : handleFault()......");
        return true;
    }

    @Override
    public void close(MessageContext context) {
        System.out.println("Client : close()......");
    }

    @Override
    public Set<QName> getHeaders() {
        System.out.println("Client : getHeaders()......");
        return null;
    }

    //return current client mac address
    private String getMACAddress() throws SOAPException {

        InetAddress ip;
        StringBuilder sb = new StringBuilder();


        try {


            ip = InetAddress.getLocalHost();
            System.out.println("Current IP address : " + ip.getHostAddress());

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            byte[] mac = network.getHardwareAddress();

            System.out.print("Current MAC address : ");

            for (int i = 0; i < mac.length; i++) {

                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));

            }
            System.out.println(sb.toString());

        } catch (UnknownHostException e) {

            e.printStackTrace();

        } catch (SocketException e){

            e.printStackTrace();

        }

        return sb.toString();
    }
}
