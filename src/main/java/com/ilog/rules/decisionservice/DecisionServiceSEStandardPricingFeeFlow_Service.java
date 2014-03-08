
package com.ilog.rules.decisionservice;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "DecisionServiceSE_Standard_Pricing_Fee_Flow", targetNamespace = "http://www.ilog.com/rules/DecisionService", wsdlLocation = "file:/Users/pinthip/Documents/Projects/TMB/SE-LOS/2-Design/Data%20Mapping/BRMS/SE_Standard_Pricing_Fee_Flow.wsdl")
public class DecisionServiceSEStandardPricingFeeFlow_Service
    extends Service
{

    private final static URL DECISIONSERVICESESTANDARDPRICINGFEEFLOW_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(com.ilog.rules.decisionservice.DecisionServiceSEStandardPricingFeeFlow_Service.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = com.ilog.rules.decisionservice.DecisionServiceSEStandardPricingFeeFlow_Service.class.getResource(".");
            url = new URL(baseUrl, "file:/Users/pinthip/Documents/Projects/TMB/SE-LOS/2-Design/Data%20Mapping/BRMS/SE_Standard_Pricing_Fee_Flow.wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'file:/Users/pinthip/Documents/Projects/TMB/SE-LOS/2-Design/Data%20Mapping/BRMS/SE_Standard_Pricing_Fee_Flow.wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        DECISIONSERVICESESTANDARDPRICINGFEEFLOW_WSDL_LOCATION = url;
    }

    public DecisionServiceSEStandardPricingFeeFlow_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public DecisionServiceSEStandardPricingFeeFlow_Service() {
        super(DECISIONSERVICESESTANDARDPRICINGFEEFLOW_WSDL_LOCATION, new QName("http://www.ilog.com/rules/DecisionService", "DecisionServiceSE_Standard_Pricing_Fee_Flow"));
    }

    /**
     * 
     * @return
     *     returns DecisionServiceSEStandardPricingFeeFlow
     */
    @WebEndpoint(name = "DecisionServiceSOAPstmbrmsred1")
    public DecisionServiceSEStandardPricingFeeFlow getDecisionServiceSOAPstmbrmsred1() {
        return super.getPort(new QName("http://www.ilog.com/rules/DecisionService", "DecisionServiceSOAPstmbrmsred1"), DecisionServiceSEStandardPricingFeeFlow.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns DecisionServiceSEStandardPricingFeeFlow
     */
    @WebEndpoint(name = "DecisionServiceSOAPstmbrmsred1")
    public DecisionServiceSEStandardPricingFeeFlow getDecisionServiceSOAPstmbrmsred1(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.ilog.com/rules/DecisionService", "DecisionServiceSOAPstmbrmsred1"), DecisionServiceSEStandardPricingFeeFlow.class, features);
    }

}
