package com.clevel.selos.integration.corebanking;

import com.clevel.selos.integration.RM;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.model.CustomerInfo;
import com.clevel.selos.model.RMmodel.IndividualModel;
import com.clevel.selos.model.RMmodel.SearchIndividual;
import org.slf4j.Logger;

import javax.inject.Inject;

@RM
public class RMInterfaceImpl implements RMInterface {
    @Inject
    Logger log;

    @Inject
    RmService rmService;

    @Inject
    public RMInterfaceImpl() {
    }

    @Override
    public CustomerInfo getCustomerInfo(String id, CustomerType customerType, DocumentType documentType) throws Exception {
        SearchIndividual searchIndividual = new SearchIndividual();

        searchIndividual.setCustId("");
        IndividualModel individualModel = rmService.intiIndividual(searchIndividual);

        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setCitizenId(individualModel.getCustId());
        return  customerInfo;
    }
}
