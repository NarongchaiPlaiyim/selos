
package com.tmb.common.data.eaisearchindividualcustomercm;

import com.tmb.common.data.requestsearchindividualcustomer.ReqSearchIndividualCustomer;
import com.tmb.common.data.responsesearchindividualcustomer.ResSearchIndividualCustomer;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.0
 * 
 */
@WebService(name = "EAISearchIndividualCustomerCM", targetNamespace = "http://data.common.tmb.com/EAISearchIndividualCustomerCM/")
public interface EAISearchIndividualCustomerCM {


    /**
     * 
     * @param requestData
     * @return
     *     returns com.tmb.common.data.responsesearchindividualcustomer.ResSearchIndividualCustomer
     */
    @WebMethod(action = "searchIndividualCustomer")
    @WebResult(name = "responseData", targetNamespace = "")
    @RequestWrapper(localName = "searchIndividualCustomer", targetNamespace = "http://data.common.tmb.com/EAISearchIndividualCustomerCM/", className = "com.tmb.common.data.eaisearchindividualcustomercm.SearchIndividualCustomer")
    @ResponseWrapper(localName = "searchIndividualCustomerResponse", targetNamespace = "http://data.common.tmb.com/EAISearchIndividualCustomerCM/", className = "com.tmb.common.data.eaisearchindividualcustomercm.SearchIndividualCustomerResponse")
    public ResSearchIndividualCustomer searchIndividualCustomer(
        @WebParam(name = "requestData", targetNamespace = "")
        ReqSearchIndividualCustomer requestData);

}
