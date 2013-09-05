package com.clevel.selos.integration;

import com.clevel.selos.integration.model.CustomerInfo;

public interface RMInterface {
    enum CustomerType {INDIVIDUAL,JURISTIC}
    enum DocumentType {CITIZEN_ID,REGISTRATION_ID}
    public CustomerInfo getCustomerInfo(String reqid,String type,String custId ,CustomerType customerType,DocumentType documentType) throws Exception;
}
