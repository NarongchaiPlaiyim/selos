
package com.clevel.selos.integration.brms.service.standardpricing.interestrules;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "DecisionServiceSE_Standard_Pricing_Interest_Flow", targetNamespace = "http://www.ilog.com/rules/DecisionService", wsdlLocation = "file:/G:/3/StandardPricingInterestRules/SE_Standard_Pricing_Interest_Flow.wsdl")
public class DecisionServiceSEStandardPricingInterestFlow_Service
    extends Service
{

    private final static URL DECISIONSERVICESESTANDARDPRICINGINTERESTFLOW_WSDL_LOCATION;
    private final static WebServiceException DECISIONSERVICESESTANDARDPRICINGINTERESTFLOW_EXCEPTION;
    private final static QName DECISIONSERVICESESTANDARDPRICINGINTERESTFLOW_QNAME = new QName("http://www.ilog.com/rules/DecisionService", "DecisionServiceSE_Standard_Pricing_Interest_Flow");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/G:/3/StandardPricingInterestRules/SE_Standard_Pricing_Interest_Flow.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        DECISIONSERVICESESTANDARDPRICINGINTERESTFLOW_WSDL_LOCATION = url;
        DECISIONSERVICESESTANDARDPRICINGINTERESTFLOW_EXCEPTION = e;
    }

    public DecisionServiceSEStandardPricingInterestFlow_Service() {
        super(__getWsdlLocation(), DECISIONSERVICESESTANDARDPRICINGINTERESTFLOW_QNAME);
    }

    public DecisionServiceSEStandardPricingInterestFlow_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), DECISIONSERVICESESTANDARDPRICINGINTERESTFLOW_QNAME, features);
    }

    public DecisionServiceSEStandardPricingInterestFlow_Service(URL wsdlLocation) {
        super(wsdlLocation, DECISIONSERVICESESTANDARDPRICINGINTERESTFLOW_QNAME);
    }

    public DecisionServiceSEStandardPricingInterestFlow_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, DECISIONSERVICESESTANDARDPRICINGINTERESTFLOW_QNAME, features);
    }

    public DecisionServiceSEStandardPricingInterestFlow_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public DecisionServiceSEStandardPricingInterestFlow_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns DecisionServiceSEStandardPricingInterestFlow
     */
    @WebEndpoint(name = "DecisionServiceSOAPstmbrmsred1")
    public DecisionServiceSEStandardPricingInterestFlow getDecisionServiceSOAPstmbrmsred1() {
        return super.getPort(new QName("http://www.ilog.com/rules/DecisionService", "DecisionServiceSOAPstmbrmsred1"), DecisionServiceSEStandardPricingInterestFlow.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns DecisionServiceSEStandardPricingInterestFlow
     */
    @WebEndpoint(name = "DecisionServiceSOAPstmbrmsred1")
    public DecisionServiceSEStandardPricingInterestFlow getDecisionServiceSOAPstmbrmsred1(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.ilog.com/rules/DecisionService", "DecisionServiceSOAPstmbrmsred1"), DecisionServiceSEStandardPricingInterestFlow.class, features);
    }

    private static URL __getWsdlLocation() {
        if (DECISIONSERVICESESTANDARDPRICINGINTERESTFLOW_EXCEPTION!= null) {
            throw DECISIONSERVICESESTANDARDPRICINGINTERESTFLOW_EXCEPTION;
        }
        return DECISIONSERVICESESTANDARDPRICINGINTERESTFLOW_WSDL_LOCATION;
    }

}
