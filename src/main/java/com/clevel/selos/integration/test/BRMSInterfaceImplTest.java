package com.clevel.selos.integration.test;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.BRMSInterface;
import com.clevel.selos.integration.brms.model.request.BRMSApplicationInfo;
import com.clevel.selos.integration.brms.model.response.DocAppraisalResponse;
import com.clevel.selos.integration.brms.model.response.DocCustomerResponse;
import com.clevel.selos.integration.brms.model.response.StandardPricingResponse;
import com.clevel.selos.integration.brms.model.response.UWRulesResponse;
import org.slf4j.Logger;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

@Alternative
public class BRMSInterfaceImplTest implements BRMSInterface {
    @Inject
    @BRMS
    Logger log;

    @Inject
    public BRMSInterfaceImplTest() {
    }

    @Override
    public UWRulesResponse checkPreScreenRule(BRMSApplicationInfo applicationInfo) throws ValidationException {
        log.debug("checkPreScreenRule : applicationInfo {}", applicationInfo);
        if (applicationInfo == null) {
            log.error("preScreenRequest is null for request");
            throw new ValidationException("002");
        }


        return null;
        //return preScreenResponseList;
    }

    @Override
    public UWRulesResponse checkFullApplicationRule(BRMSApplicationInfo fullApplicationRequest) throws ValidationException {
        log.debug("checkFullApplicationRule : fullApplicationRequest {}", fullApplicationRequest);
        if (fullApplicationRequest == null) {
            log.error("fullApplicationRequest is null for request");
            throw new ValidationException("002");
        }
        //todo call service
        return new UWRulesResponse();
    }

    @Override
    public StandardPricingResponse checkStandardPricingIntRule(BRMSApplicationInfo standardPricingIntRequest) throws ValidationException {
        log.debug("checkStandardPricingIntRule : standardPricingIntRequest {}", standardPricingIntRequest);
        if (standardPricingIntRequest == null) {
            log.error("standardPricingIntRequest is null for request");
            throw new ValidationException("002");
        }
        //todo call service
        return new StandardPricingResponse();
    }

    @Override
    public StandardPricingResponse checkStandardPricingFeeRule(BRMSApplicationInfo standardPricingFeeRequest) throws ValidationException {
        log.debug("checkStandardPricingFeeRule : standardPricingFeeRequest {}", standardPricingFeeRequest);
        if (standardPricingFeeRequest == null) {
            log.error("standardPricingFeeRequest is null for request");
            throw new ValidationException("002");
        }
        //todo call service
        return new StandardPricingResponse();
    }

    @Override
    public DocCustomerResponse checkDocCustomerRule(BRMSApplicationInfo docCustomerRequest) throws ValidationException {
        log.debug("checkDocCustomerRule : docCustomerRequest {}", docCustomerRequest);
        if (docCustomerRequest == null) {
            log.error("docCustomerRequest is null for request");
            throw new ValidationException("002");
        }
        //todo call service
        return new DocCustomerResponse();
    }

    @Override
    public DocAppraisalResponse checkDocAppraisalRule(BRMSApplicationInfo docAppraisalRequest) throws ValidationException {
        log.debug("checkDocAppraisalRule : docAppraisalRequest {}", docAppraisalRequest);
        if (docAppraisalRequest == null) {
            log.error("docAppraisalRequest is null for request");
            throw new ValidationException("002");
        }
        //todo call service
        return new DocAppraisalResponse();
    }
}
