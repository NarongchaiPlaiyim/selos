package com.clevel.selos.integration;

import com.clevel.selos.model.RMmodel.customeraccount.CustomerAccountModel;
import com.clevel.selos.model.view.CustomerInfoView;

public interface RMInterface {


    enum DocumentType {CITIZEN_ID,PASSPORT,CORPORATE_ID}
    enum SearchBy {CUSTOMER_ID,TMBCUS_ID}

    public CustomerInfoView getIndividualInfo(String userId,String customerId,DocumentType documentType,SearchBy searchBy)throws Exception;
    public CustomerInfoView getCorporateInfo(String userId,String customerId,DocumentType documentType,SearchBy searchBy)throws Exception;
    public CustomerAccountModel getCustomerAccountInfo(String userId,String customerId,DocumentType documentType,SearchBy searchBy) throws Exception;
}
