package com.clevel.selos.integration;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.brms.model.request.*;
import com.clevel.selos.integration.brms.model.response.*;

import java.util.List;

public interface BRMSInterface {
    public List<PreScreenResponse> checkPreScreenRule(PreScreenRequest preScreenRequest) throws ValidationException;
    public List<FullApplicationResponse> checkFullApplicationRule(FullApplicationRequest fullApplicationRequest) throws ValidationException;
    public List<StandardPricingIntResponse> checkStandardPricingIntRule(StandardPricingIntRequest standardPricingIntRequest) throws ValidationException;
    public List<StandardPricingFeeResponse> checkStandardPricingFeeRule(StandardPricingFeeRequest standardPricingFeeRequest) throws ValidationException;
    public List<DocCustomerResponse> checkDocCustomerRule(DocCustomerRequest docCustomerRequest) throws ValidationException;
    public List<DocAppraisalResponse> checkDocAppraisalRule(DocAppraisalRequest docAppraisalRequest) throws ValidationException;
}
