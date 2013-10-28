package com.clevel.selos.integration.brms;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.BRMSInterface;
import com.clevel.selos.integration.brms.model.RuleColorResult;
import com.clevel.selos.integration.brms.model.request.*;
import com.clevel.selos.integration.brms.model.response.*;
import org.slf4j.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Default
public class BRMSInterfaceImpl implements BRMSInterface, Serializable {
    @Inject
    @BRMS
    Logger log;

    @Inject
    public BRMSInterfaceImpl() {
    }

    @Override
    public List<PreScreenResponse> checkPreScreenRule(PreScreenRequest preScreenRequest) throws ValidationException {
        log.debug("checkPreScreenRule : preScreenRequest {}", preScreenRequest);
        if (preScreenRequest == null) {
            log.error("preScreenRequest is null for request");
            throw new ValidationException("002");
        }

        List<PreScreenResponse> preScreenResponseList = new ArrayList<PreScreenResponse>();

        PreScreenResponse preScreenResponse = new PreScreenResponse();
        preScreenResponse.setRuleName("Group_Result_Final_AppLevel");
        preScreenResponse.setRuleOrder("2010");
        preScreenResponse.setType("Group Result");
        preScreenResponse.setPersonalId("");
        preScreenResponse.setColor(RuleColorResult.GREEN);
        preScreenResponse.setDeviationFlag("No Deviation");
        preScreenResponse.setRejectGroupCode("P");
        preScreenResponseList.add(preScreenResponse);

        PreScreenResponse preScreenResponse2 = new PreScreenResponse();
        preScreenResponse2.setRuleName("Decision_Matrix_NCB_DB");
        preScreenResponse2.setRuleOrder("1010");
        preScreenResponse2.setType("");
        preScreenResponse2.setPersonalId("1234567890123");
        preScreenResponse2.setColor(RuleColorResult.RED);
        preScreenResponse2.setDeviationFlag("");
        preScreenResponse2.setRejectGroupCode("");
        preScreenResponseList.add(preScreenResponse2);

        return preScreenResponseList;
    }

    @Override
    public List<FullApplicationResponse> checkFullApplicationRule(FullApplicationRequest fullApplicationRequest) throws ValidationException {
        log.debug("checkFullApplicationRule : fullApplicationRequest {}", fullApplicationRequest);
        if (fullApplicationRequest == null) {
            log.error("fullApplicationRequest is null for request");
            throw new ValidationException("002");
        }
        //todo call service
        return new ArrayList<FullApplicationResponse>();
    }

    @Override
    public List<StandardPricingIntResponse> checkStandardPricingIntRule(StandardPricingIntRequest standardPricingIntRequest) throws ValidationException {
        log.debug("checkStandardPricingIntRule : standardPricingIntRequest {}", standardPricingIntRequest);
        if (standardPricingIntRequest == null) {
            log.error("standardPricingIntRequest is null for request");
            throw new ValidationException("002");
        }
        //todo call service
        return new ArrayList<StandardPricingIntResponse>();
    }

    @Override
    public List<StandardPricingFeeResponse> checkStandardPricingFeeRule(StandardPricingFeeRequest standardPricingFeeRequest) throws ValidationException {
        log.debug("checkStandardPricingFeeRule : standardPricingFeeRequest {}", standardPricingFeeRequest);
        if (standardPricingFeeRequest == null) {
            log.error("standardPricingFeeRequest is null for request");
            throw new ValidationException("002");
        }
        //todo call service
        return new ArrayList<StandardPricingFeeResponse>();
    }

    @Override
    public List<DocCustomerResponse> checkDocCustomerRule(DocCustomerRequest docCustomerRequest) throws ValidationException {
        log.debug("checkDocCustomerRule : docCustomerRequest {}", docCustomerRequest);
        if (docCustomerRequest == null) {
            log.error("docCustomerRequest is null for request");
            throw new ValidationException("002");
        }
        //todo call service
        return new ArrayList<DocCustomerResponse>();
    }

    @Override
    public List<DocAppraisalResponse> checkDocAppraisalRule(DocAppraisalRequest docAppraisalRequest) throws ValidationException {
        log.debug("checkDocAppraisalRule : docAppraisalRequest {}", docAppraisalRequest);
        if (docAppraisalRequest == null) {
            log.error("docAppraisalRequest is null for request");
            throw new ValidationException("002");
        }
        //todo call service
        return new ArrayList<DocAppraisalResponse>();
    }
}
