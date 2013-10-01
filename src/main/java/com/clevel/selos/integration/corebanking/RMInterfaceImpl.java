package com.clevel.selos.integration.corebanking;

import com.clevel.selos.integration.RM;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.model.RMmodel.*;
import com.clevel.selos.model.RMmodel.corporateInfo.CorporateModel;
import com.clevel.selos.model.RMmodel.customeraccount.CustomerAccountModel;
import com.clevel.selos.model.RMmodel.customeraccount.SearchCustomerAccountModel;
import com.clevel.selos.model.RMmodel.individualInfo.IndividualModel;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.system.Config;
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
    TranformCorporate tranformCorporate;

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

    private String documentTypeValue;
    private String searchByValue;
    @Override
    public CustomerInfoView getIndividualInfo(String userId,String customerId, DocumentType documentType,SearchBy searchBy) throws Exception {

        log.debug("getIndividualInfo()");
        SearchIndividual searchIndividual = new SearchIndividual();

        if(DocumentType.CITIZEN_ID==documentType){
            documentTypeValue="CI";
        }else if(DocumentType.PASSPORT==documentType){
            documentTypeValue="PP";
        }

        if(SearchBy.CUSTOMER_ID==searchBy){
            searchByValue="card";
        }else if(SearchBy.TMBCUS_ID==searchBy){
            searchByValue="code";
        }

        searchIndividual.setReqId("generateBySystem");
        searchIndividual.setCustType("P");
        searchIndividual.setType(documentTypeValue);
        searchIndividual.setCustNbr(customerId);
        searchIndividual.setCustId(customerId);
        searchIndividual.setCustName("");
        searchIndividual.setCustSurname("");
        searchIndividual.setRadSelectSearch(searchByValue);
        log.debug("RequestValue : {} ",searchIndividual.toString());

        IndividualModel individualModel = rmService.individualService(searchIndividual,userId);


        return tranformIndividual.tranform(individualModel);
    }

    @Override
    public CustomerInfoView getCorporateInfo(String userId,String customerId, DocumentType documentType,SearchBy searchBy) throws Exception {

        if(DocumentType.CORPORATE_ID==documentType){
            documentTypeValue="SC";
        }else{
            documentTypeValue="SC";
        }

        if(SearchBy.CUSTOMER_ID==searchBy){
            searchByValue="card";
        }else if(SearchBy.TMBCUS_ID==searchBy){
            searchByValue="code";
        }

        log.debug("getCorporateInfo()");
        SearchIndividual searchIndividual = new SearchIndividual();
        searchIndividual.setReqId("generateBySystem");
        searchIndividual.setCustType("C");
        searchIndividual.setType(documentTypeValue);
        searchIndividual.setCustNbr(customerId);
        searchIndividual.setCustId(customerId);
        searchIndividual.setCustName("");
        searchIndividual.setRadSelectSearch(searchByValue);
        log.debug("requestValue : {}",searchIndividual.toString());
        CorporateModel corporateModel = rmService.corporateService(searchIndividual,userId);

        return tranformCorporate.tranform(corporateModel);
    }

    @Override
    public CustomerAccountModel getCustomerAccountInfo(String userId,String customerId) throws Exception {

        log.debug("getCustomerAccountInfo()");
        SearchCustomerAccountModel searchCustomerAccountModel = new SearchCustomerAccountModel();
        searchCustomerAccountModel.setReqId("generateBySystem");
        searchCustomerAccountModel.setAcronym(acronym);
        searchCustomerAccountModel.setProductCode(productCode);
//        searchCustomerAccountModel.setServerURL(serverURL);
//        searchCustomerAccountModel.setSessionId(sessionId);
        searchCustomerAccountModel.setCustNbr(customerId);
        searchCustomerAccountModel.setRadSelectSearch("code");
        log.debug("RequestValue : {}",searchCustomerAccountModel.toString());
        CustomerAccountModel customerAccountModel = rmService.customerAccountService(searchCustomerAccountModel,userId);

        return  customerAccountModel;
    }
}
