package com.clevel.selos.integration.brms.convert;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.brms.model.request.BRMSApplicationInfo;
import org.slf4j.Logger;

import javax.inject.Inject;

public class DocCustomerConverter extends Converter{

    @Inject
    @SELOS
    Logger logger;

    @Inject
    public DocCustomerConverter(){
    }



}
