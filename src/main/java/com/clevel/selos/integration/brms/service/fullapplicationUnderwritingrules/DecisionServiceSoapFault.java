
package com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "DecisionServiceException", targetNamespace = "http://www.ilog.com/rules/DecisionService")
public class DecisionServiceSoapFault
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private DecisionServiceException faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public DecisionServiceSoapFault(String message, DecisionServiceException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public DecisionServiceSoapFault(String message, DecisionServiceException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: two.DecisionServiceException
     */
    public DecisionServiceException getFaultInfo() {
        return faultInfo;
    }

}
