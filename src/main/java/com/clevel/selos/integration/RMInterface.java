package com.clevel.selos.integration;

import com.clevel.selos.integration.model.CustomerInfo;
import com.clevel.selos.model.CAmodel.CustomerAccountModel;
import com.clevel.selos.model.RMmodel.CorporateModel;
import com.clevel.selos.model.RMmodel.IndividualModel;

public interface RMInterface {
    enum DocumentType {CITIZEN_ID,REGISTRATION_ID}

    public IndividualModel getIndividualInfo(String reqid,String type,String custId,DocumentType documentType)throws Exception;
    public CorporateModel getCorporateInfo(String reqid,String type,String custId,DocumentType documentType)throws Exception;
    public CustomerAccountModel getCustomerAccount(String reqid,String type,String custId ,DocumentType documentType) throws Exception;
}
