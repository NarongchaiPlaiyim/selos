package com.clevel.selos.businesscontrol;

import com.clevel.selos.integration.BRMSInterface;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class BRMSControl extends BusinessControl {

    @Inject
    BRMSInterface brmsInterface;

    public void checkCriteria(String workcaseId){

    }

    public void getPriceFee(){

    }

    public void getMandateDoc(){

    }

}
