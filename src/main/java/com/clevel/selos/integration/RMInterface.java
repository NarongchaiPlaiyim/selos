package com.clevel.selos.integration;

import com.clevel.selos.model.RMmodel.CustomerAccountModel;
import com.clevel.selos.model.RMmodel.corporateInfo.CorporateModel;
import com.clevel.selos.model.view.CustomerInfoView;

public interface RMInterface {


    enum DocumentType {CITIZEN_ID,PASSPORT}
    enum SearchBy {CITIZEN_ID,TMBCUS_ID}

    public CustomerInfoView getIndividualInfo(String tmbCusID,String registrationID,DocumentType documentType,SearchBy searchBy)throws Exception;
    public CustomerInfoView getCorporateInfo(String tmbCusID,String registrationID,DocumentType documentType,SearchBy searchBy)throws Exception;
    public CustomerAccountModel getCustomerAccountInfo(String tmbCusID,String registrationID,DocumentType documentType,SearchBy searchBy) throws Exception;
}
