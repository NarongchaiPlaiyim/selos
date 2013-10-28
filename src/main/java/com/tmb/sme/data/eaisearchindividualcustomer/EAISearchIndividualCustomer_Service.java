
package com.tmb.sme.data.eaisearchindividualcustomer;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.0
 */
@WebServiceClient(name = "EAISearchIndividualCustomer",
        targetNamespace = "http://data.sme.tmb.com/EAISearchIndividualCustomer/",
        wsdlLocation = "/com/tmb/EAISearchIndividualCustomer.wsdl")
public class EAISearchIndividualCustomer_Service
        extends Service {

    private final static URL EAISEARCHINDIVIDUALCUSTOMER_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(com.tmb.sme.data.eaisearchindividualcustomer.EAISearchIndividualCustomer_Service.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = com.tmb.sme.data.eaisearchindividualcustomer.EAISearchIndividualCustomer_Service.class.getResource(".");
            url = new URL(baseUrl, "file:/D:/Project-Clevel/selos/src/main/java/EAISearchIndividualCustomer.wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'file:/D:/Project-Clevel/selos/src/main/java/EAISearchIndividualCustomer.wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        EAISEARCHINDIVIDUALCUSTOMER_WSDL_LOCATION = url;
    }

    public EAISearchIndividualCustomer_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public EAISearchIndividualCustomer_Service() {
        super(EAISEARCHINDIVIDUALCUSTOMER_WSDL_LOCATION, new QName("http://data.sme.tmb.com/EAISearchIndividualCustomer/", "EAISearchIndividualCustomer"));
    }

    /**
     * @return returns EAISearchIndividualCustomer
     */
    @WebEndpoint(name = "EAISearchIndividualCustomer")
    public EAISearchIndividualCustomer getEAISearchIndividualCustomer() {
        return super.getPort(new QName("http://data.sme.tmb.com/EAISearchIndividualCustomer/", "EAISearchIndividualCustomer"), EAISearchIndividualCustomer.class);
    }

}
