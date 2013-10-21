
package com.tmb.sme.data.eaisearchcorporatecustomer;

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
 * 
 */
@WebServiceClient(name = "EAISearchCorporateCustomer", targetNamespace = "http://data.sme.tmb.com/EAISearchCorporateCustomer/", wsdlLocation = "file:/D:/Project-Clevel/selos/src/main/java/EAISearchCorporateCustomer.wsdl")
public class EAISearchCorporateCustomer_Service
    extends Service
{

    private final static URL EAISEARCHCORPORATECUSTOMER_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(com.tmb.sme.data.eaisearchcorporatecustomer.EAISearchCorporateCustomer_Service.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = com.tmb.sme.data.eaisearchcorporatecustomer.EAISearchCorporateCustomer_Service.class.getResource(".");
            url = new URL(baseUrl, "file:/D:/Project-Clevel/selos/src/main/java/EAISearchCorporateCustomer.wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'file:/D:/Project-Clevel/selos/src/main/java/EAISearchCorporateCustomer.wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        EAISEARCHCORPORATECUSTOMER_WSDL_LOCATION = url;
    }

    public EAISearchCorporateCustomer_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public EAISearchCorporateCustomer_Service() {
        super(EAISEARCHCORPORATECUSTOMER_WSDL_LOCATION, new QName("http://data.sme.tmb.com/EAISearchCorporateCustomer/", "EAISearchCorporateCustomer"));
    }

    /**
     * 
     * @return
     *     returns EAISearchCorporateCustomer
     */
    @WebEndpoint(name = "EAISearchCorporateCustomer")
    public EAISearchCorporateCustomer getEAISearchCorporateCustomer() {
        return super.getPort(new QName("http://data.sme.tmb.com/EAISearchCorporateCustomer/", "EAISearchCorporateCustomer"), EAISearchCorporateCustomer.class);
    }

}
