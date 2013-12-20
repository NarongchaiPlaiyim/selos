
package com.clevel.selos.integration.brms.service.standardpricing.interestrules;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebService(name = "DecisionServiceSE_Standard_Pricing_Interest_Flow", targetNamespace = "http://www.ilog.com/rules/DecisionService")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface DecisionServiceSEStandardPricingInterestFlow {


    /**
     * 
     * @param parameters
     * @return
     *     returns interest.DecisionServiceResponse
     * @throws DecisionServiceSoapFault
     */
    @WebMethod(action = "executeDecisionService")
    @WebResult(name = "DecisionServiceResponse", targetNamespace = "http://www.ilog.com/rules/DecisionService", partName = "parameters")
    public DecisionServiceResponse executeDecisionService(
        @WebParam(name = "DecisionServiceRequest", targetNamespace = "http://www.ilog.com/rules/DecisionService", partName = "parameters")
        DecisionServiceRequest parameters)
        throws DecisionServiceSoapFault
    ;

}
