package com.clevel.selos.integration.brms.convert;

import com.clevel.selos.integration.brms.model.request.*;

public interface ConvertInf {
    public void convertInputModelToRequestModel(PreScreenRequest inputModel)throws Exception;
    public void convertInputModelToRequestModel(FullApplicationRequest inputModel) throws Exception;
    public void convertInputModelToRequestModel(DocCustomerRequest inputModel) throws Exception;
    public void convertInputModelToRequestModel(DocAppraisalRequest inputModel) throws Exception;
    public void convertInputModelToRequestModel(StandardPricingIntRequest inputModel) throws Exception;
    public void convertInputModelToRequestModel(StandardPricingFeeRequest inputModel) throws Exception;
}
