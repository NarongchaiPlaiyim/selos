package com.clevel.selos.integration;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.brms.model.request.BRMSApplicationInfo;
import com.clevel.selos.integration.brms.model.response.DocAppraisalResponse;
import com.clevel.selos.integration.brms.model.response.DocCustomerResponse;
import com.clevel.selos.integration.brms.model.response.StandardPricingResponse;
import com.clevel.selos.integration.brms.model.response.UWRulesResponse;

public interface BRMSInterface {
    public UWRulesResponse checkPreScreenRule(BRMSApplicationInfo applicationInfo) throws ValidationException;

    public UWRulesResponse checkFullApplicationRule(BRMSApplicationInfo applicationInfo) throws ValidationException;

    public StandardPricingResponse checkStandardPricingIntRule(BRMSApplicationInfo applicationInfo) throws ValidationException;

    public StandardPricingResponse checkStandardPricingFeeRule(BRMSApplicationInfo applicationInfo) throws ValidationException;

    public DocCustomerResponse checkDocCustomerRule(BRMSApplicationInfo applicationInfo) throws ValidationException;

    public DocAppraisalResponse checkDocAppraisalRule(BRMSApplicationInfo applicationInfo) throws ValidationException;
}
