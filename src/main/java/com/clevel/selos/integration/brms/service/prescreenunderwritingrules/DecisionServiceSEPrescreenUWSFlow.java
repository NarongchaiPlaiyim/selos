
package com.clevel.selos.integration.brms.service.prescreenunderwritingrules;

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
@WebService(name = "DecisionServiceSE_Prescreen_UWS_Flow", targetNamespace = "http://www.ilog.com/rules/DecisionService")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface DecisionServiceSEPrescreenUWSFlow {


    /**
     * 
     * @param parameters
     * @return
     *     returns one.DecisionServiceResponse
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
