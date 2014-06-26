package com.clevel.selos.integration.ncb;

import com.clevel.selos.exception.NCBInterfaceException;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.NCBInterface;
import com.clevel.selos.integration.ncb.letter.RejectLetterService;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSInputModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSOutputModel;
import com.clevel.selos.integration.ncb.nccrs.service.NCCRSService;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSInputModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSOutputModel;
import com.clevel.selos.integration.ncb.ncrs.service.NCRSService;
import com.clevel.selos.integration.ncb.vaildation.ValidationImp;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;

public class NCBInterfaceImpl implements NCBInterface, Serializable {
    @Inject
    @NCB
    Logger log;

    @Inject
    NCCRSService nccrsService;

    @Inject
    NCRSService ncrsService;

    @Inject
    RejectLetterService rejectLetterService;

    @Inject
    ValidationImp validationImp;

    @Inject
    @ExceptionMessage
    Message msg;

    @Inject
    public NCBInterfaceImpl() {
    }

    @Override
    public ArrayList<NCRSOutputModel> request(NCRSInputModel inputModel) throws Exception {
        log.info("NCRS request");
        validationImp.validation(inputModel);
        return ncrsService.process(inputModel);
    }

    @Override
    public ArrayList<NCCRSOutputModel> request(NCCRSInputModel inputModel) throws Exception {
        log.info("NCCRS request");
        validationImp.validation(inputModel);
        return nccrsService.process(inputModel);
    }

    public void generateRejectedLetter(String userId, long workCaseId){
        //TODO: add audit log
        try{
            rejectLetterService.extractRejectedLetterData(userId, workCaseId);
        } catch (NCBInterfaceException e){
            log.error("Exception while generate rejected letter data!", e);
            throw e;
        } catch (Exception e) {
            log.error("Exception while generate rejected letter data!", e);
            String exceptionMessage = msg.get(ExceptionMapping.NCB_EXCEPTION);
            if(!Util.isEmpty(e.getMessage()) && !e.getMessage().trim().equalsIgnoreCase("null")){
                exceptionMessage = e.getMessage();
            }
            throw new NCBInterfaceException(e, ExceptionMapping.NCB_EXCEPTION, exceptionMessage);
        }
    }
}
