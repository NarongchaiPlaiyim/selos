package com.clevel.selos.transform;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.UWRuleResultDetail;
import com.clevel.selos.model.db.working.UWRuleResultSummary;
import com.clevel.selos.model.view.CustomerInfoSimpleView;
import com.clevel.selos.model.view.UWRuleResultDetailView;
import com.clevel.selos.model.view.UWRuleResultSummaryView;
import org.slf4j.Logger;

import javax.inject.Inject;

public class UWRuleResultTransform extends Transform{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private CustomerTransform customerTransform;

    @Inject
    public UWRuleResultTransform(){}

    public UWRuleResultSummaryView transformToView(UWRuleResultSummary uwRuleResultSummary){

        return null;
    }

    public UWRuleResultSummary transformToModel(UWRuleResultSummaryView uwRuleResultSummaryView){
        return null;

    }

    public UWRuleResultDetailView transformToView(UWRuleResultDetail uwRuleResultDetail){
        if(uwRuleResultDetail == null)
            return null;

        UWRuleResultDetailView uwRuleResultDetailView = new UWRuleResultDetailView();
        uwRuleResultDetailView.setId(uwRuleResultDetail.getId());

        CustomerInfoSimpleView customerInfoSimpleView = customerTransform.transformToSimpleView(uwRuleResultDetail.getCustomer());
        uwRuleResultDetailView.setCustomerInfoSimpleView(customerInfoSimpleView);



        return null;

    }

    public UWRuleResultDetail transformToModel(UWRuleResultDetailView uwRuleResultDetailView){
        return null;

    }





}
