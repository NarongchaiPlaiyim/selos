package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.transform.CustomerTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class CreditFacProposeControl extends BusinessControl {
    @Inject
    @SELOS
    Logger log;

    @Inject
    CustomerTransform customerTransform;

    @Inject
    CustomerDAO customerDAO;

    public CreditFacProposeControl(){

    }

}