package com.clevel.selos.integration.brms.convert;

import com.clevel.selos.integration.brms.model.request.*;

public interface ConvertInf {
    public void convertInputModelToRequestModel(PreScreenRequest inputModel) throws Exception;

    public com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.DecisionServiceRequest convertInputModelToRequestModel(FullApplicationRequest inputModel) throws Exception;

    public com.clevel.selos.integration.brms.service.document.customerrules.DecisionServiceRequest convertInputModelToRequestModel(DocCustomerRequest inputModel) throws Exception;

    public com.clevel.selos.integration.brms.service.document.apprisalrules.DecisionServiceRequest convertInputModelToRequestModel(DocAppraisalRequest inputModel) throws Exception;

    public com.clevel.selos.integration.brms.service.standardpricing.interestrules.DecisionServiceRequest convertInputModelToRequestModel(StandardPricingIntRequest inputModel) throws Exception;

    public com.clevel.selos.integration.brms.service.standardpricing.feerules.DecisionServiceRequest convertInputModelToRequestModel(StandardPricingFeeRequest inputModel) throws Exception;
}
