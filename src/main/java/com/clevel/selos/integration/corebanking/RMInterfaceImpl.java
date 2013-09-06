package com.clevel.selos.integration.corebanking;

import com.clevel.selos.integration.RM;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.model.RMmodel.CustomerAccountModel;
import com.clevel.selos.model.RMmodel.CorporateModel;
import com.clevel.selos.model.RMmodel.IndividualModel;
import com.clevel.selos.model.RMmodel.SearchIndividual;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.io.Serializable;

@Default
public class RMInterfaceImpl implements RMInterface ,Serializable{
    @Inject
    @RM
    Logger log;

    @Inject
    RMService rmService;

    @Inject
    public RMInterfaceImpl() {

    }
    @PostConstruct
    public void onCreation(){

    }

    @Override
    public IndividualModel getIndividualInfo(String reqId, String type, String custId, DocumentType documentType) throws Exception {

        log.debug("::::::::::::::::::::::::::::::::::::  getIndividualInfo()");
        SearchIndividual searchIndividual = new SearchIndividual();
        searchIndividual.setReqId(reqId);
        searchIndividual.setCustType("P");
        searchIndividual.setType("CI");
        searchIndividual.setCustId(custId);
        searchIndividual.setRadSelectSearch("card");
        log.debug("::::::::::::::::::::::::::::::::::::  RequestValue : {} ",searchIndividual.toString());

        IndividualModel individualModel = rmService.IndividualService(searchIndividual);
        return individualModel;
    }

    @Override
    public CorporateModel getCorporateInfo(String reqId, String type, String custId, DocumentType documentType) throws Exception {

        log.debug("::::::::::::::::::::::::::::::::::::  getCorporateInfo()");
        SearchIndividual searchIndividual = new SearchIndividual();
        searchIndividual.setReqId(reqId);
        searchIndividual.setCustType("C");
        searchIndividual.setType("CI");
        searchIndividual.setCustId(custId);
        searchIndividual.setRadSelectSearch("card");
        log.debug("::::::::::::::::::::::::::::::::::::  RequestValue : {}",searchIndividual.toString());
        CorporateModel corporateModel = rmService.CorporateService(searchIndividual);
        return corporateModel;
    }

    @Override
    public CustomerAccountModel getCustomerAccountInfo(String reqId, String type, String custNbr, DocumentType documentType) throws Exception {

        log.debug("::::::::::::::::::::::::::::::::::::  getCustomerAccountInfo()");
        SearchIndividual searchIndividual = new SearchIndividual();
        searchIndividual.setReqId(reqId);
        searchIndividual.setCustNbr(custNbr);
        searchIndividual.setRadSelectSearch("code");
        log.debug("::::::::::::::::::::::::::::::::::::  RequestValue : {}",searchIndividual.toString());
        CustomerAccountModel customerAccountModel = rmService.CustomerAccountService(searchIndividual);

        return  customerAccountModel;
    }
}
