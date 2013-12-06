package com.clevel.selos.integration.coms;

import com.clevel.selos.exception.COMSInterfaceException;
import com.clevel.selos.integration.COMS;
import com.clevel.selos.integration.COMSInterface;
import com.clevel.selos.integration.coms.model.AppraisalData;
import com.clevel.selos.integration.coms.model.AppraisalDataResult;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import java.io.Serializable;

public class COMSInterfaceImpl implements COMSInterface, Serializable {
    @Inject
    @COMS
    org.slf4j.Logger log;

    @Inject
    @ExceptionMessage
    Message msg;

    @Inject
    COMSService comsService;

    @Inject
    public COMSInterfaceImpl() {

    }

    public AppraisalDataResult getAppraisalData(String userId, String jobNo){
        AppraisalDataResult appraisalDataResult = new AppraisalDataResult();
        //TODO: add audit log
        try{
            AppraisalData appraisalData = comsService.getAppraisalData(userId,jobNo);
            appraisalDataResult.setActionResult(ActionResult.SUCCESS);
            appraisalDataResult.setAppraisalData(appraisalData);
        } catch (COMSInterfaceException e){
            log.error("Exception while get COMS Appraisal data!", e);
            throw e;
        } catch (Exception e) {
            log.error("Exception while get CSI data!", e);
            String exceptionMessage = msg.get(ExceptionMapping.COMS_EXCEPTION);
            if(!Util.isEmpty(e.getMessage()) && !e.getMessage().trim().equalsIgnoreCase("null")){
                exceptionMessage = e.getMessage();
            }
            throw new COMSInterfaceException(e, ExceptionMapping.COMS_EXCEPTION, exceptionMessage);
        }
        return appraisalDataResult;
    }
}
