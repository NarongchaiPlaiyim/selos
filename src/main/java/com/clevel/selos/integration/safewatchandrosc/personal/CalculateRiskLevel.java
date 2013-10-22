package com.clevel.selos.integration.safewatchandrosc.personal;


import com.clevel.selos.controller.CalculateRiskLevelPage;
import com.tmb.common.data.eaicalrisknewpersonal.EAICalRiskNewPersonal;
import com.tmb.common.data.eaicalrisknewpersonal.EAICalRiskNewPersonal_Service;
import com.tmb.common.data.requestcalrisknewpersonal.Body;
import com.tmb.common.data.requestcalrisknewpersonal.Header;
import com.tmb.common.data.requestcalrisknewpersonal.ReqCalRiskNewPersonal;
import com.tmb.common.data.responsecalrisknewpersonal.ResCalRiskNewPersonal;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;


public class CalculateRiskLevel implements Serializable {
    @Inject
    Logger log;
    public CalculateRiskLevel() {

    }

    public String  process(CalculateRiskLevelPage calculateRiskLevelPage){
        System.out.println("process()");

        log.info("=========================================process()");
        log.info("========================================="+calculateRiskLevelPage.toString());
        log.info("========================================= reqId : "+calculateRiskLevelPage.getReqId());
        log.info("========================================= productCode : "+calculateRiskLevelPage.getProductCode());
        log.info("========================================= acronym : "+calculateRiskLevelPage.getAcronym());
        log.info("========================================= selectorFlag : "+calculateRiskLevelPage.getSelectorFlag());
        log.info("========================================= firstName : "+calculateRiskLevelPage.getFirstName());
        log.info("========================================= lastName : "+calculateRiskLevelPage.getLastName());
        log.info("========================================= cardId : "+calculateRiskLevelPage.getCardId());

        ReqCalRiskNewPersonal reqPersonal = new ReqCalRiskNewPersonal();

        Header reqHeader = new Header();
        reqHeader.setReqId(calculateRiskLevelPage.getReqId());
        reqHeader.setProductCode(calculateRiskLevelPage.getProductCode());
        reqHeader.setAcronym(calculateRiskLevelPage.getAcronym());
        reqHeader.setSelectorFlag(calculateRiskLevelPage.getSelectorFlag());

        reqPersonal.setHeader(reqHeader);
        Body reqBody = new Body();
        reqBody.setFirstName(calculateRiskLevelPage.getFirstName());
        reqBody.setLastName(calculateRiskLevelPage.getLastName());
        reqBody.setCardId(calculateRiskLevelPage.getCardId());


        try {

            URL url = new URL("/WEB-INF/wsdl/RMIndividual/EAICalRiskNewPersonal.wsdl");
            QName qname = new QName("http://data.sme.tmb.com/EAISearchIndividualCustomer", "EAISearchIndividualCustomer");

            /*EAISearchIndividualCustomer_Service service = new EAISearchIndividualCustomer_Service(url,qname);*/
            //EAISearchIndividualCustomer eaiSearchInd = service.getPort(EAISearchIndividualCustomer.class);

            EAICalRiskNewPersonal_Service service = new EAICalRiskNewPersonal_Service();
            EAICalRiskNewPersonal port = service.getEAICalRiskNewPersonal();

            ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    "http://10.175.140.18:7824/services/EAICalRiskNewPersonal");

            reqPersonal.setBody(reqBody);
            ResCalRiskNewPersonal resPersonal = port.calRiskNewPersonal(reqPersonal);

            System.out.println("getResCode()"+resPersonal.getHeader().getResCode());
            System.out.println("getReqId()"+resPersonal.getHeader().getReqId());
            return resPersonal.getHeader().getResCode();
        } catch (MalformedURLException e) {
            System.out.println("Error : " + e);
        }

         return "Test";
    }
}
