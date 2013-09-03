
package com.tmb.common.data.eaisearchcustomeraccount;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.jws.HandlerChain;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.0
 * 
 */
@WebServiceClient(name = "EAISearchCustomerAccount", targetNamespace = "http://data.common.tmb.com/EAISearchCustomerAccount/", wsdlLocation = "file:/D:/Project-Clevel/selos/src/main/java/EAISearchCustomerAccount.wsdl")
@HandlerChain(file = "/LogMessage_handlerCustomerAccount.xml")
public class EAISearchCustomerAccount_Service
    extends Service
{

    private final static URL EAISEARCHCUSTOMERACCOUNT_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(com.tmb.common.data.eaisearchcustomeraccount.EAISearchCustomerAccount_Service.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = com.tmb.common.data.eaisearchcustomeraccount.EAISearchCustomerAccount_Service.class.getResource(".");
            url = new URL(baseUrl, "file:/D:/Project-Clevel/selos/src/main/java/EAISearchCustomerAccount.wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'file:/D:/Project-Clevel/selos/src/main/java/EAISearchCustomerAccount.wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        EAISEARCHCUSTOMERACCOUNT_WSDL_LOCATION = url;
    }

    public EAISearchCustomerAccount_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public EAISearchCustomerAccount_Service() {
        super(EAISEARCHCUSTOMERACCOUNT_WSDL_LOCATION, new QName("http://data.common.tmb.com/EAISearchCustomerAccount/", "EAISearchCustomerAccount"));
    }

    /**
     * 
     * @return
     *     returns EAISearchCustomerAccount
     */
    @WebEndpoint(name = "EAISearchCustomerAccount")
    public EAISearchCustomerAccount getEAISearchCustomerAccount() {
        return super.getPort(new QName("http://data.common.tmb.com/EAISearchCustomerAccount/", "EAISearchCustomerAccount"), EAISearchCustomerAccount.class);
    }

}
