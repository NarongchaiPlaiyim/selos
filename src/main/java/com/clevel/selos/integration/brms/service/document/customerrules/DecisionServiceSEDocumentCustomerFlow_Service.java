
package com.clevel.selos.integration.brms.service.document.customerrules;

import javax.xml.namespace.QName;
import javax.xml.ws.*;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01
 * Generated source version: 2.2
 */

@WebServiceClient(name = "DecisionServiceSE_Document_Customer_Flow", targetNamespace = "http://www.ilog.com/rules/DecisionService", wsdlLocation = "http://stmbrmsred1:9080/DecisionService/ws/SE_Document_Customer_RuleApp/1.0/SE_Document_Customer_Flow?WSDL")
public class DecisionServiceSEDocumentCustomerFlow_Service
        extends Service {

    private final static URL DECISIONSERVICESEDOCUMENTCUSTOMERFLOW_WSDL_LOCATION;
    private final static WebServiceException DECISIONSERVICESEDOCUMENTCUSTOMERFLOW_EXCEPTION;
    private final static QName DECISIONSERVICESEDOCUMENTCUSTOMERFLOW_QNAME = new QName("http://www.ilog.com/rules/DecisionService", "DecisionServiceSE_Document_Customer_Flow");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://stmbrmsred1:9080/DecisionService/ws/SE_Document_Customer_RuleApp/1.0/SE_Document_Customer_Flow?WSDL");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        DECISIONSERVICESEDOCUMENTCUSTOMERFLOW_WSDL_LOCATION = url;
        DECISIONSERVICESEDOCUMENTCUSTOMERFLOW_EXCEPTION = e;
    }

    public DecisionServiceSEDocumentCustomerFlow_Service() {
        super(__getWsdlLocation(), DECISIONSERVICESEDOCUMENTCUSTOMERFLOW_QNAME);
    }

    public DecisionServiceSEDocumentCustomerFlow_Service(URL wsdlLocation) {
        super(wsdlLocation, DECISIONSERVICESEDOCUMENTCUSTOMERFLOW_QNAME);
    }

    public DecisionServiceSEDocumentCustomerFlow_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    /**
     * @return returns DecisionServiceSEDocumentCustomerFlow
     */
    @WebEndpoint(name = "DecisionServiceSOAPstmbrmsred1")
    public DecisionServiceSEDocumentCustomerFlow getDecisionServiceSOAPstmbrmsred1() {
        return super.getPort(new QName("http://www.ilog.com/rules/DecisionService", "DecisionServiceSOAPstmbrmsred1"), DecisionServiceSEDocumentCustomerFlow.class);
    }

    /**
     * @param features A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return returns DecisionServiceSEDocumentCustomerFlow
     */
    @WebEndpoint(name = "DecisionServiceSOAPstmbrmsred1")
    public DecisionServiceSEDocumentCustomerFlow getDecisionServiceSOAPstmbrmsred1(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.ilog.com/rules/DecisionService", "DecisionServiceSOAPstmbrmsred1"), DecisionServiceSEDocumentCustomerFlow.class, features);
    }

    private static URL __getWsdlLocation() {
        if (DECISIONSERVICESEDOCUMENTCUSTOMERFLOW_EXCEPTION != null) {
            throw DECISIONSERVICESEDOCUMENTCUSTOMERFLOW_EXCEPTION;
        }
        return DECISIONSERVICESEDOCUMENTCUSTOMERFLOW_WSDL_LOCATION;
    }

}
