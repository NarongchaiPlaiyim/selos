package com.clevel.selos.integration;

import com.clevel.selos.model.RMmodel.CustomerAccountModel;
import com.clevel.selos.model.RMmodel.corporateInfo.CorporateModel;
import com.clevel.selos.model.view.CustomerInfoView;

public interface RMInterface {


    enum DocumentType {CITIZEN_ID,REGISTRATION_ID}

    public CustomerInfoView getIndividualInfo(String reqId,String type,String custId,DocumentType documentType)throws Exception;
    public CorporateModel getCorporateInfo(String reqId,String type,String custId,DocumentType documentType)throws Exception;
    public CustomerAccountModel getCustomerAccountInfo(String reqId,String type,String custId ,DocumentType documentType) throws Exception;
}
