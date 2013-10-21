
package com.tmb.sme.data.eaisearchcorporatecustomer;

import com.tmb.sme.data.requestsearchcorporatecustomer.ReqSearchCorporateCustomer;
import com.tmb.sme.data.responsesearchcorporatecustomer.ResSearchCorporateCustomer;

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
@WebService(name = "EAISearchCorporateCustomer", targetNamespace = "http://data.sme.tmb.com/EAISearchCorporateCustomer/")
public interface EAISearchCorporateCustomer {


    /**
     * 
     * @param requestData
     * @return
     *     returns com.tmb.sme.data.responsesearchcorporatecustomer.ResSearchCorporateCustomer
     */
    @WebMethod(action = "searchCorporateCustomer")
    @WebResult(name = "responseData", targetNamespace = "")
    @RequestWrapper(localName = "searchCorporateCustomer", targetNamespace = "http://data.sme.tmb.com/EAISearchCorporateCustomer/", className = "com.tmb.sme.data.eaisearchcorporatecustomer.SearchCorporateCustomer")
    @ResponseWrapper(localName = "searchCorporateCustomerResponse", targetNamespace = "http://data.sme.tmb.com/EAISearchCorporateCustomer/", className = "com.tmb.sme.data.eaisearchcorporatecustomer.SearchCorporateCustomerResponse")
    public ResSearchCorporateCustomer searchCorporateCustomer(
        @WebParam(name = "requestData", targetNamespace = "")
        ReqSearchCorporateCustomer requestData);

}
