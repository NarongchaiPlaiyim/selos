package com.clevel.selos.integration.corebanking;


import com.clevel.selos.model.RMmodel.corporateInfo.CorporateModel;
import com.clevel.selos.model.view.CustomerInfoView;

import java.io.Serializable;

public class TranformCorporate implements Serializable{


    public CustomerInfoView tranform(CorporateModel corporateModel){

        CustomerInfoView customerInfoView=new CustomerInfoView();

//        customerInfoView.setEducation(corporateModel.getRegistrationAddress().ge);



        return customerInfoView;
    }
}
