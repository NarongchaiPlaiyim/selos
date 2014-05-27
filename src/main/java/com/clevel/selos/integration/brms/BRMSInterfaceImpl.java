package com.clevel.selos.integration.brms;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.BRMSInterface;
import com.clevel.selos.integration.brms.convert.*;
import com.clevel.selos.integration.brms.model.request.BRMSApplicationInfo;
import com.clevel.selos.integration.brms.model.response.DocAppraisalResponse;
import com.clevel.selos.integration.brms.model.response.DocCustomerResponse;
import com.clevel.selos.integration.brms.model.response.StandardPricingResponse;
import com.clevel.selos.integration.brms.model.response.UWRulesResponse;
import com.clevel.selos.integration.brms.service.EndPoint;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.util.Util;
import com.ilog.rules.decisionservice.DecisionServiceResponse;
import org.slf4j.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.io.Serializable;

@Default
public class BRMSInterfaceImpl implements BRMSInterface, Serializable {
    @Inject
    @BRMS
    Logger logger;

    @Inject
    EndPoint endpoint;
    @Inject
    StandardPricingFeeConverter standardPricingFeeConverter;
    @Inject
    StandardPricingIntConverter standardPricingIntConverter;
    @Inject
    PrescreenConverter prescreenConverter;
    @Inject
    FullApplicationConverter fullApplicationConverter;
    @Inject
    DocCustomerConverter docCustomerConverter;
    @Inject
    DocAppraisalConverter docAppraisalConverter;

    @Inject
    public BRMSInterfaceImpl() {
    }

    @Override
    public UWRulesResponse checkPreScreenRule(BRMSApplicationInfo applicationInfo) throws ValidationException {
        logger.debug("checkPreScreenRule : applicationInfo {}", applicationInfo);
        if (applicationInfo == null) {
            logger.error("preScreenRequest is null for request");
            throw new ValidationException("002");
        }

        UWRulesResponse uwRulesResponse = null;

        try {
            DecisionServiceResponse decisionServiceResponse = endpoint.callPrescreenUnderwritingRulesService(prescreenConverter.getDecisionServiceRequest(applicationInfo));
            uwRulesResponse = prescreenConverter.getUWRulesResponse(decisionServiceResponse);
            uwRulesResponse.setActionResult(ActionResult.SUCCESS);
        }catch (Exception ex) {
            uwRulesResponse = new UWRulesResponse();
            uwRulesResponse.setActionResult(ActionResult.FAILED);
            uwRulesResponse.setReason(Util.getMessageException(ex));
            logger.error("checkPreScreenRule calling exception/convert exception", ex);
        }

        return uwRulesResponse;
    }

    @Override
    public UWRulesResponse checkFullApplicationRule(BRMSApplicationInfo applicationInfo) throws ValidationException {
        logger.debug("checkFullApplicationRule : applicationInfo {}", applicationInfo);
        if (applicationInfo == null) {
            logger.error("fullApplicationRequest is null for request");
            throw new ValidationException("002");
        }

        UWRulesResponse uwRulesResponse = null;

        try {
            DecisionServiceResponse decisionServiceResponse = endpoint.callFullApplicationUnderwritingRulesService(fullApplicationConverter.getDecisionServiceRequest(applicationInfo));
            uwRulesResponse = fullApplicationConverter.getUWRulesResponse(decisionServiceResponse);
            uwRulesResponse.setActionResult(ActionResult.SUCCESS);
        }catch (Exception ex) {
            uwRulesResponse = new UWRulesResponse();
            uwRulesResponse.setActionResult(ActionResult.FAILED);
            uwRulesResponse.setReason(Util.getMessageException(ex));
            logger.error("checkFullApplicationRule calling exception/convert exception", ex);
        }
        return uwRulesResponse;
    }

    @Override
    public StandardPricingResponse checkStandardPricingIntRule(BRMSApplicationInfo applicationInfo) throws ValidationException {
        logger.debug("checkStandardPricingIntRule : standardPricingIntRequest {}", applicationInfo);
        if (applicationInfo == null) {
            logger.error("standardPricingIntRequest is null for request");
            throw new ValidationException("002");
        }

        StandardPricingResponse standardPricingResponse = new StandardPricingResponse();

        try{
            DecisionServiceResponse decisionServiceResponse = endpoint.callStandardPricingInterestRulesService(standardPricingIntConverter.getDecisionServiceRequest(applicationInfo));
            standardPricingResponse = standardPricingIntConverter.getStandardPricingResponse(decisionServiceResponse);
            standardPricingResponse.setActionResult(ActionResult.SUCCESS);

        }catch (Exception ex){
            standardPricingResponse.setActionResult(ActionResult.FAILED);
            standardPricingResponse.setReason(Util.getMessageException(ex));
            logger.error("checkStandardPricingIntRule calling exception/convert exception", ex);
        }

        return standardPricingResponse;
    }

    @Override
    public StandardPricingResponse checkStandardPricingFeeRule(BRMSApplicationInfo applicationInfo) throws ValidationException {
        logger.debug("checkStandardPricingFeeRule : applicationInfo {}", applicationInfo);
        if (applicationInfo == null) {
            logger.error("standardPricingFeeRequest is null for request");
            throw new ValidationException("002");
        }

        StandardPricingResponse standardPricingResponse = new StandardPricingResponse();
        try{
            DecisionServiceResponse decisionServiceResponse = endpoint.callStandardPricingFeeRulesService(standardPricingFeeConverter.getDecisionServiceRequest(applicationInfo));
            standardPricingResponse = standardPricingFeeConverter.getStandardPricingResponse(decisionServiceResponse);
            standardPricingResponse.setActionResult(ActionResult.SUCCESS);

        }catch (Exception ex){
            standardPricingResponse.setActionResult(ActionResult.FAILED);
            standardPricingResponse.setReason(Util.getMessageException(ex));
            logger.error("checkStandardPricingFeeRule calling exception or convert exception", ex);
        }

        return standardPricingResponse;
    }

    @Override
    public DocCustomerResponse checkDocCustomerRule(BRMSApplicationInfo applicationInfo) throws ValidationException {
        logger.debug("checkDocCustomerRule : applicationInfo {}", applicationInfo);
        if (applicationInfo == null) {
            logger.error("docCustomerRequest is null for request");
            throw new ValidationException("002");
        }

        DocCustomerResponse docCustomerResponse = new DocCustomerResponse();
        try{
            DecisionServiceResponse decisionServiceResponse = endpoint.callDocumentCustomerRulesService(docCustomerConverter.getDecisionServiceRequest(applicationInfo));
            docCustomerResponse = docCustomerConverter.getDocCustomerResponse(decisionServiceResponse);
            docCustomerResponse.setActionResult(ActionResult.SUCCESS);

        }catch (Exception ex){
            docCustomerResponse.setActionResult(ActionResult.FAILED);
            docCustomerResponse.setReason(Util.getMessageException(ex));
            logger.error("checkDocCustomerRule calling exception or convert exception", ex);
        }
        return docCustomerResponse;
    }

    @Override
    public DocAppraisalResponse checkDocAppraisalRule(BRMSApplicationInfo applicationInfo) throws ValidationException {
        logger.debug("checkDocAppraisalRule : applicationInfo {}", applicationInfo);
        if (applicationInfo == null) {
            logger.error("docAppraisalRequest is null for request");
            throw new ValidationException("002");
        }

        DocAppraisalResponse docAppraisalResponse = new DocAppraisalResponse();
        try{
            DecisionServiceResponse decisionServiceResponse = endpoint.callDocumentAppraisalRulesService(docAppraisalConverter.getDecisionServiceRequest(applicationInfo));
            docAppraisalResponse = docAppraisalConverter.getDocAppraisalResponse(decisionServiceResponse);
            docAppraisalResponse.setActionResult(ActionResult.SUCCESS);
        } catch (Exception ex){
            docAppraisalResponse.setActionResult(ActionResult.FAILED);
            docAppraisalResponse.setReason(Util.getMessageException(ex));
            logger.error("checkDocAppraisalRule calling exception or convert exception", ex);
        }
        logger.debug("-- end checkDocAppraisalRule() return {}", docAppraisalResponse);
        return new DocAppraisalResponse();
    }
}
