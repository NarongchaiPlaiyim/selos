package com.clevel.selos.integration.corebanking;

import com.clevel.selos.integration.RM;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.model.CustomerInfo;
import com.clevel.selos.model.RMmodel.IndividualModel;
import com.clevel.selos.model.RMmodel.SearchIndividual;
import org.slf4j.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

@Default
public class RMInterfaceImpl implements RMInterface {
    @Inject
    @RM
    Logger log;

    @Inject
    RmService rmService;

    @Inject
    public RMInterfaceImpl() {
        log.info("=== RMInterfaceImpl()");
    }

    @Override
    public CustomerInfo getCustomerInfo(String reqid, String type, String custId, CustomerType customerType, DocumentType documentType) throws Exception {
        SearchIndividual searchIndividual = new SearchIndividual();

        searchIndividual.setReqId(reqid);

        if(customerType==CustomerType.INDIVIDUAL){
            searchIndividual.setCustType("P");
        }else{
            searchIndividual.setCustType("C");
        }
        searchIndividual.setType(type);
        searchIndividual.setRadSelectSearch("card");

        IndividualModel individualModel = rmService.intiIndividual(searchIndividual);

        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setCitizenId(individualModel.getCustId());
        return  customerInfo;
    }
}
