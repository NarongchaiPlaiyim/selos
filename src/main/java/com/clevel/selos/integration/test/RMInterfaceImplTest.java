package com.clevel.selos.integration.test;

import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.model.CustomerInfo;
import org.slf4j.Logger;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

@Alternative
public class RMInterfaceImplTest implements RMInterface {
    @Inject
    Logger log;

    @Inject
    public RMInterfaceImplTest() {
    }

    @Override
    public CustomerInfo getCustomerInfo(String id, CustomerType customerType, DocumentType documentType) throws Exception {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setCitizenId("12345");


        return customerInfo;
    }
}
