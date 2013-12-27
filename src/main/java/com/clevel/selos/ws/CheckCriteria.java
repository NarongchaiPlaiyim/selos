package com.clevel.selos.ws;


import org.slf4j.Logger;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public class CheckCriteria {

    @Inject
    @WS
    Logger log;


    @Inject
    public CheckCriteria() {

    }


    @WebMethod
    public CheckCriteriaResponse caseCriteria(@WebParam(name = "appId") String appId) {
    log.debug("caseCriteriaService()");
        CheckCriteriaResponse criteriaResponse = new CheckCriteriaResponse();

        try {

            criteriaResponse.setValue(WSResponse.DUPLICATE_CA, appId);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return criteriaResponse;
    }


}
