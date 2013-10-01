package com.clevel.selos.integration.corebanking;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.RM;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.corebanking.model.SearchIndividual;
import com.clevel.selos.integration.corebanking.model.corporateInfo.CorporateModel;
import com.clevel.selos.integration.corebanking.model.corporateInfo.CorporateResult;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountResult;
import com.clevel.selos.integration.corebanking.model.customeraccount.SearchCustomerAccountModel;
import com.clevel.selos.integration.corebanking.model.individualInfo.IndividualModel;
import com.clevel.selos.integration.corebanking.model.individualInfo.IndividualResult;
import com.clevel.selos.model.ActionResult;
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
    public IndividualResult getIndividualInfo(String userId,String customerId, DocumentType documentType,SearchBy searchBy) throws Exception {

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
        IndividualModel individualModel = null;
        IndividualResult individualResult;
        try{
            individualModel = rmService.individualService(searchIndividual,userId);
            individualResult = new IndividualResult();
            individualResult.setCustomerId(customerId);
            individualResult.setActionResult(ActionResult.SUCCEED);
            individualResult.setIndividualModel(individualModel);
        } catch (ValidationException ex) {
            individualResult = new IndividualResult();
            individualResult.setCustomerId(customerId);
            individualResult.setActionResult(ActionResult.FAILED);
            individualResult.setReason(ex.getMessage());
        } catch (Exception ex2){
            individualResult = new IndividualResult();
            individualResult.setCustomerId(customerId);
            individualResult.setActionResult(ActionResult.FAILED);
            if(ex2.getMessage()!=null){
                individualResult.setReason(ex2.getMessage());
            } else {
                individualResult.setReason(ActionResult.EXCEPTION.toString());
            }
        }

        return individualResult;
    }

    @Override
    public CorporateResult getCorporateInfo(String userId,String customerId, DocumentType documentType,SearchBy searchBy) throws Exception {

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

        CorporateModel corporateModel = null;
        CorporateResult corporateResult = new CorporateResult();
        try{
            corporateModel = rmService.corporateService(searchIndividual, userId);
            corporateResult.setCustomerId(customerId);
            corporateResult.setActionResult(ActionResult.SUCCEED);
            corporateResult.setCorporateModel(corporateModel);
        } catch (ValidationException ex) {
            corporateResult.setCustomerId(customerId);
            corporateResult.setActionResult(ActionResult.FAILED);
            corporateResult.setReason(ex.getMessage());
        } catch (Exception ex2){
            corporateResult.setCustomerId(customerId);
            corporateResult.setActionResult(ActionResult.FAILED);
            if(ex2.getMessage()!=null){
                corporateResult.setReason(ex2.getMessage());
            } else {
                corporateResult.setReason(ActionResult.EXCEPTION.toString());
            }
        }

        return corporateResult;
    }

    @Override
    public CustomerAccountResult getCustomerAccountInfo(String userId,String customerId) throws Exception {

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
        CustomerAccountResult customerAccountResult = new CustomerAccountResult();
        try{
            customerAccountResult = rmService.customerAccountService(searchCustomerAccountModel,userId);
        } catch (ValidationException ex) {
            customerAccountResult.setCustomerId(customerId);
            customerAccountResult.setActionResult(ActionResult.FAILED);
            customerAccountResult.setReason(ex.getMessage());
        } catch (Exception ex2){
            customerAccountResult.setCustomerId(customerId);
            customerAccountResult.setActionResult(ActionResult.FAILED);
            if(ex2.getMessage()!=null){
                customerAccountResult.setReason(ex2.getMessage());
            } else {
                customerAccountResult.setReason(ActionResult.EXCEPTION.toString());
            }
        }
        return customerAccountResult;
    }
}
