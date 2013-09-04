package com.clevel.selos.integration.brms.service;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.brms.model.request.*;
import com.clevel.selos.integration.brms.model.response.*;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;

public class BRMSService implements Serializable{
    @Inject
    @BRMS
    Logger log;
    @Inject
    @ValidationMessage
    Message validationMsg;

    public PreScreenResponse checkPreScreenRule(PreScreenRequest preScreenRequest) throws ValidationException{
        log.debug("checkPreScreenRule : preScreenRequest {}",preScreenRequest);
        if(preScreenRequest==null){
            log.error("preScreenRequest is null for request");
            throw new ValidationException(validationMsg.get("validation.002"));
        }
        return new PreScreenResponse();
    }

    public FullApplicationResponse checkFullApplicationRule(FullApplicationRequest fullApplicationRequest) throws ValidationException{
        log.debug("checkFullApplicationRule : fullApplicationRequest {}",fullApplicationRequest);
        if(fullApplicationRequest==null){
            log.error("fullApplicationRequest is null for request");
            throw new ValidationException(validationMsg.get("validation.002"));
        }
        return new FullApplicationResponse();
    }

    public StandardPricingIntResponse checkStandardPricingIntRule(StandardPricingIntRequest standardPricingIntRequest) throws ValidationException{
        log.debug("checkStandardPricingIntRule : standardPricingIntRequest {}",standardPricingIntRequest);
        if(standardPricingIntRequest==null){
            log.error("standardPricingIntRequest is null for request");
            throw new ValidationException(validationMsg.get("validation.002"));
        }
        return new StandardPricingIntResponse();
    }

    public StandardPricingFeeResponse checkStandardPricingFeeRule(StandardPricingFeeRequest standardPricingFeeRequest) throws ValidationException{
        log.debug("checkStandardPricingFeeRule : standardPricingFeeRequest {}",standardPricingFeeRequest);
        if(standardPricingFeeRequest==null){
            log.error("standardPricingFeeRequest is null for request");
            throw new ValidationException(validationMsg.get("validation.002"));
        }
        return new StandardPricingFeeResponse();
    }

    public DocCustomerResponse checkDocCustomerRule(DocCustomerRequest docCustomerRequest) throws ValidationException{
        log.debug("checkDocCustomerRule : docCustomerRequest {}",docCustomerRequest);
        if(docCustomerRequest==null){
            log.error("docCustomerRequest is null for request");
            throw new ValidationException(validationMsg.get("validation.002"));
        }
        return new DocCustomerResponse();
    }

    public DocAppraisalResponse checkDocAppraisalRule(DocAppraisalRequest docAppraisalRequest) throws ValidationException{
        log.debug("checkDocAppraisalRule : docAppraisalRequest {}",docAppraisalRequest);
        if(docAppraisalRequest==null){
            log.error("docAppraisalRequest is null for request");
            throw new ValidationException(validationMsg.get("validation.002"));
        }
        return new DocAppraisalResponse();
    }
}
