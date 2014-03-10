package com.clevel.selos.integration;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.brms.model.request.BRMSApplicationInfo;
import com.clevel.selos.integration.brms.model.response.*;
import com.clevel.selos.integration.brms.model.response.StandardPricingResponse;

import java.util.List;

public interface BRMSInterface {
    public UWRulesResponse checkPreScreenRule(BRMSApplicationInfo applicationInfo) throws ValidationException;

    public UWRulesResponse checkFullApplicationRule(BRMSApplicationInfo applicationInfo) throws ValidationException;

    public StandardPricingResponse checkStandardPricingIntRule(BRMSApplicationInfo applicationInfo) throws ValidationException;

    public StandardPricingResponse checkStandardPricingFeeRule(BRMSApplicationInfo applicationInfo) throws ValidationException;

    public DocCustomerResponse checkDocCustomerRule(BRMSApplicationInfo applicationInfo) throws ValidationException;

    public DocAppraisalResponse checkDocAppraisalRule(BRMSApplicationInfo applicationInfo) throws ValidationException;
}
