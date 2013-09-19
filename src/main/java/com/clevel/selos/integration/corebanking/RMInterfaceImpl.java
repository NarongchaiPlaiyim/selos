package com.clevel.selos.integration.corebanking;

import com.clevel.selos.integration.RM;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.model.RMmodel.*;
import com.clevel.selos.model.RMmodel.corporateInfo.CorporateModel;
import com.clevel.selos.model.RMmodel.individualInfo.IndividualModel;
import com.clevel.selos.model.view.AddressView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.system.Config;
import com.clevel.selos.util.Util;
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
    TranformIndividual tranformIndividual;

    @Inject
    @Config(name = "interface.rm.customerAccount.acronym")
    String acronym;

    @Inject
    @Config(name = "interface.rm.customerAccount.productCode")
    String productCode;

    @Inject
    @Config(name = "interface.rm.customerAccount.acronym")
    String serverURL;

    @Inject
    @Config(name = "interface.rm.customerAccount.productCode")
    String sessionId;

    @Inject
    public RMInterfaceImpl() {

    }
    @PostConstruct
    public void onCreation(){

    }

    @Override
    public CustomerInfoView
    getIndividualInfo(String reqId, String type, String custId, DocumentType documentType) throws Exception {

        log.debug("getIndividualInfo()");
        SearchIndividual searchIndividual = new SearchIndividual();
        searchIndividual.setReqId(reqId);
        searchIndividual.setCustType("P");
        searchIndividual.setType("CI");
        searchIndividual.setCustNbr("");
        searchIndividual.setCustId(custId);
        searchIndividual.setCustName("");
        searchIndividual.setCustSurname("");
        searchIndividual.setRadSelectSearch("card");
        log.debug("RequestValue : {} ",searchIndividual.toString());

        IndividualModel individualModel = rmService.individualService(searchIndividual);


        return tranformIndividual.tranform(individualModel);
    }

    @Override
    public CorporateModel getCorporateInfo(String reqId, String type, String custId, DocumentType documentType) throws Exception {

        log.debug("getCorporateInfo()");
        SearchIndividual searchIndividual = new SearchIndividual();
        searchIndividual.setReqId(reqId);
        searchIndividual.setCustType("C");
        searchIndividual.setType("CI");
        searchIndividual.setCustNbr("");
        searchIndividual.setCustId(custId);
        searchIndividual.setCustName("");
        searchIndividual.setRadSelectSearch("card");
        log.debug("requestValue : {}",searchIndividual.toString());
        CorporateModel corporateModel = rmService.corporateService(searchIndividual);
        return corporateModel;
    }

    @Override
    public CustomerAccountModel getCustomerAccountInfo(String reqId, String type, String custNbr, DocumentType documentType) throws Exception {

        log.debug("getCustomerAccountInfo()");
        SearchCustomerAccountModel searchCustomerAccountModel = new SearchCustomerAccountModel();
        searchCustomerAccountModel.setReqId(reqId);
        searchCustomerAccountModel.setAcronym(acronym);
        searchCustomerAccountModel.setProductCode(productCode);
//        searchCustomerAccountModel.setServerURL(serverURL);
//        searchCustomerAccountModel.setSessionId(sessionId);
        searchCustomerAccountModel.setCustNbr(custNbr);
        searchCustomerAccountModel.setRadSelectSearch("code");
        log.debug("RequestValue : {}",searchCustomerAccountModel.toString());
        CustomerAccountModel customerAccountModel = rmService.customerAccountService(searchCustomerAccountModel);

        return  customerAccountModel;
    }
}
