package com.clevel.selos.integration.test;

import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.test.RMTest;
import com.clevel.selos.integration.model.CustomerInfo;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;

@RMTest
public class RMInterfaceImplTest implements RMInterface ,Serializable{
    @Inject
    Logger log;

    @Inject
    public RMInterfaceImplTest() {
    }

    @Override
    public CustomerInfo getCustomerInfo(String reqid, String type, String custId, CustomerType customerType, DocumentType documentType) throws Exception {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setCitizenId("3100300390029");
        customerInfo.setFirstName("พสุ");
        customerInfo.setLastName("กุญ");
        customerInfo.setAddressNumber("");
        customerInfo.setAge(22);
        customerInfo.setDateOfBirth(new Date());
        customerInfo.setTmbCustomerId("00000001180946");


        return customerInfo;
    }
}
