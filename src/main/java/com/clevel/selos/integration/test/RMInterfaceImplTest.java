package com.clevel.selos.integration.test;

import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.model.CAmodel.CustomerAccountModel;
import com.clevel.selos.model.RMmodel.CorporateModel;
import com.clevel.selos.model.RMmodel.IndividualModel;
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
    public IndividualModel getIndividualInfo(String reqId, String type, String custId, DocumentType documentType) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CorporateModel getCorporateInfo(String reqId, String type, String custId, DocumentType documentType) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CustomerAccountModel getCustomerAccount(String reqId, String type, String custId, DocumentType documentType) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
