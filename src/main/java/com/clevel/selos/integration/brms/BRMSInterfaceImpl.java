package com.clevel.selos.integration.brms;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.BRMSInterface;
import com.clevel.selos.integration.brms.convert.StandardPricingFeeConverter;
import com.clevel.selos.integration.brms.convert.StandardPricingIntConverter;
import com.clevel.selos.integration.brms.model.RuleColorResult;
import com.clevel.selos.integration.brms.model.request.BRMSApplicationInfo;
import com.clevel.selos.integration.brms.model.response.*;
import com.clevel.selos.integration.brms.model.response.StandardPricingIntResponse;
import com.clevel.selos.integration.brms.service.EndPoint;
import com.clevel.selos.model.ActionResult;
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
    EndPoint endpoint;
    @Inject
    StandardPricingFeeConverter standardPricingFeeConverter;
    @Inject
    StandardPricingIntConverter standardPricingIntConverter;

    @Inject
    public BRMSInterfaceImpl() {
    }

    @Override
    public List<PreScreenResponse> checkPreScreenRule(BRMSApplicationInfo applicationInfo) throws ValidationException {
        log.debug("checkPreScreenRule : applicationInfo {}", applicationInfo);
        if (applicationInfo == null) {
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
    public List<FullApplicationResponse> checkFullApplicationRule(BRMSApplicationInfo applicationInfo) throws ValidationException {
        log.debug("checkFullApplicationRule : applicationInfo {}", applicationInfo);
        if (applicationInfo == null) {
            log.error("fullApplicationRequest is null for request");
            throw new ValidationException("002");
        }

        return new ArrayList<FullApplicationResponse>();
    }

    @Override
    public StandardPricingIntResponse checkStandardPricingIntRule(BRMSApplicationInfo applicationInfo) throws ValidationException {
        log.debug("checkStandardPricingIntRule : standardPricingIntRequest {}", applicationInfo);
        if (applicationInfo == null) {
            log.error("standardPricingIntRequest is null for request");
            throw new ValidationException("002");
        }

        StandardPricingIntResponse standardPricingIntResponse = new StandardPricingIntResponse();

        try{
            com.clevel.selos.integration.brms.service.standardpricing.interestrules.DecisionServiceResponse decisionServiceResponse = endpoint.callStandardPricingInterestRulesService(standardPricingIntConverter.getDecisionServiceRequest(applicationInfo));
            standardPricingIntResponse = standardPricingIntConverter.getStandardPricingResponse(decisionServiceResponse);
            standardPricingIntResponse.setActionResult(ActionResult.SUCCESS);

        }catch (Exception ex){
            standardPricingIntResponse.setActionResult(ActionResult.FAILED);
            standardPricingIntResponse.setReason(ex.getMessage());

        }

        return standardPricingIntResponse;
    }

    @Override
    public List<StandardPricingFeeResponse> checkStandardPricingFeeRule(BRMSApplicationInfo applicationInfo) throws ValidationException {
        log.debug("checkStandardPricingFeeRule : applicationInfo {}", applicationInfo);
        if (applicationInfo == null) {
            log.error("standardPricingFeeRequest is null for request");
            throw new ValidationException("002");
        }

        return new ArrayList<StandardPricingFeeResponse>();
    }

    @Override
    public List<DocCustomerResponse> checkDocCustomerRule(BRMSApplicationInfo applicationInfo) throws ValidationException {
        log.debug("checkDocCustomerRule : applicationInfo {}", applicationInfo);
        if (applicationInfo == null) {
            log.error("docCustomerRequest is null for request");
            throw new ValidationException("002");
        }
        //todo call service
        return new ArrayList<DocCustomerResponse>();
    }

    @Override
    public List<DocAppraisalResponse> checkDocAppraisalRule(BRMSApplicationInfo applicationInfo) throws ValidationException {
        log.debug("checkDocAppraisalRule : applicationInfo {}", applicationInfo);
        if (applicationInfo == null) {
            log.error("docAppraisalRequest is null for request");
            throw new ValidationException("002");
        }
        //todo call service
        return new ArrayList<DocAppraisalResponse>();
    }
}
