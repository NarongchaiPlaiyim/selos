
package com.tmb.common.data.eaicalrisknewpersonal;

import com.tmb.common.data.requestcalrisknewpersonal.ReqCalRiskNewPersonal;
import com.tmb.common.data.responsecalrisknewpersonal.ResCalRiskNewPersonal;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "EAICalRiskNewPersonal", targetNamespace = "http://data.common.tmb.com/EAICalRiskNewPersonal/")
@XmlSeeAlso({
    com.tmb.common.data.requestcalrisknewpersonal.ObjectFactory.class,
    com.tmb.common.data.responsecalrisknewpersonal.ObjectFactory.class,
    com.tmb.common.data.eaicalrisknewpersonal.ObjectFactory.class
})
public interface EAICalRiskNewPersonal {


    /**
     * 
     * @param requestData
     * @return
     *     returns com.tmb.common.data.responsecalrisknewpersonal.ResCalRiskNewPersonal
     */
    @WebMethod(action = "calRiskNewPersonal")
    @WebResult(name = "responseData", targetNamespace = "")
    @RequestWrapper(localName = "calRiskNewPersonal", targetNamespace = "http://data.common.tmb.com/EAICalRiskNewPersonal/", className = "com.tmb.common.data.eaicalrisknewpersonal.CalRiskNewPersonal")
    @ResponseWrapper(localName = "calRiskNewPersonalResponse", targetNamespace = "http://data.common.tmb.com/EAICalRiskNewPersonal/", className = "com.tmb.common.data.eaicalrisknewpersonal.CalRiskNewPersonalResponse")
    public ResCalRiskNewPersonal calRiskNewPersonal(
            @WebParam(name = "requestData", targetNamespace = "")
            ReqCalRiskNewPersonal requestData);

}
