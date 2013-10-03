package com.clevel.selos.integration;

import com.clevel.selos.integration.corebanking.model.corporateInfo.CorporateResult;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountResult;
import com.clevel.selos.integration.corebanking.model.individualInfo.IndividualResult;

public interface RMInterface {


    enum DocumentType {CITIZEN_ID,PASSPORT,CORPORATE_ID}
    enum SearchBy {CUSTOMER_ID,TMBCUS_ID}

    public IndividualResult getIndividualInfo(String userId,String customerId,DocumentType documentType,SearchBy searchBy);
    public CorporateResult getCorporateInfo(String userId,String customerId,DocumentType documentType,SearchBy searchBy);
    public CustomerAccountResult getCustomerAccountInfo(String userId,String customerId);
}
