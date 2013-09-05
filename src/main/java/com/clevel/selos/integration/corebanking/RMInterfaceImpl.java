package com.clevel.selos.integration.corebanking;

import com.clevel.selos.integration.RM;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.model.CAmodel.CustomerAccountModel;
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
        log.info("=== RMInterfaceImpl()");
    }

    @Override
    public IndividualModel getIndividualInfo(String reqId, String type, String custId, DocumentType documentType) throws Exception {
        SearchIndividual searchIndividual = new SearchIndividual();
        searchIndividual.setReqId(reqId);
        searchIndividual.setCustType("P");
        searchIndividual.setType("CI");
        searchIndividual.setCustId(custId);
        searchIndividual.setRadSelectSearch("card");

        IndividualModel individualModel = rmService.intiIndividual(searchIndividual);
        return individualModel;
    }

    @Override
    public CorporateModel getCorporateInfo(String reqId, String type, String custId, DocumentType documentType) throws Exception {
        SearchIndividual searchIndividual = new SearchIndividual();
        searchIndividual.setReqId(reqId);
        searchIndividual.setCustType("C");
        searchIndividual.setType("CI");
        searchIndividual.setCustId(custId);
        searchIndividual.setRadSelectSearch("card");

        CorporateModel corporateModel = rmService.intiCorporate(searchIndividual);
        return corporateModel;
    }

    @Override
    public CustomerAccountModel getCustomerAccount(String reqId, String type, String custNbr, DocumentType documentType) throws Exception {
        SearchIndividual searchIndividual = new SearchIndividual();
        searchIndividual.setReqId(reqId);
        searchIndividual.setCustNbr(custNbr);
        searchIndividual.setRadSelectSearch("code");

        CustomerAccountModel customerAccountModel = rmService.intiCustomerAccount(searchIndividual);

        return  customerAccountModel;
    }
}
