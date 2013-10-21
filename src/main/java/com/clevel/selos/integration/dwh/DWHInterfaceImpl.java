package com.clevel.selos.integration.dwh;

import com.clevel.selos.exception.DWHInterfaceException;
import com.clevel.selos.integration.DWH;
import com.clevel.selos.integration.DWHInterface;
import com.clevel.selos.integration.dwh.bankstatement.BankStatementService;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatementResult;
import com.clevel.selos.integration.dwh.obligation.model.Obligation;
import com.clevel.selos.integration.dwh.obligation.ObligationService;
import com.clevel.selos.integration.dwh.obligation.model.ObligationResult;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import org.slf4j.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Default
public class DWHInterfaceImpl implements DWHInterface,Serializable{
    @Inject
    @DWH
    Logger log;

    @Inject
    @ExceptionMessage
    Message msg;

    @Inject
    ObligationService obligationService;
    @Inject
    BankStatementService bankStatementService;

    @Inject
    public DWHInterfaceImpl() {

    }

    @Override
    public ObligationResult getObligationData(String userId, List<String> tmbCusIdList){
        log.debug("getObligationData (userId : {}, tmbCusIdList : {})",userId,tmbCusIdList);
        ObligationResult obligationResult = new ObligationResult();
        try{
            if(tmbCusIdList!=null && tmbCusIdList.size()>0){
                obligationResult = obligationService.getObligationByTmbCusId(tmbCusIdList);
                log.debug("getObligationData result (obligationResult : {})",obligationResult);
            } else {
                obligationResult.setActionResult(ActionResult.FAILED);
                obligationResult.setReason(msg.get(ExceptionMapping.DWH_INVALID_INPUT));
                obligationResult.setObligationList(new ArrayList<Obligation>());
            }
        } catch (Exception e){
            log.error("Exception while get obligation data!", e);
            throw new DWHInterfaceException(e, ExceptionMapping.DWH_OBLIGATION_EXCEPTION,msg.get(ExceptionMapping.DWH_OBLIGATION_EXCEPTION,userId));
        }

        return obligationResult;
    }

    @Override
    public DWHBankStatementResult getBankStatementData(String userId, String accountNumber, Date fromDate, int numberOfMonth){
        log.debug("getBankStatementData (userId : {}, accountNumber : {}, fromDate : {}, numberOfMonth : {})",userId,accountNumber,fromDate,numberOfMonth);
        DWHBankStatementResult bankStatementResult = new DWHBankStatementResult();
        try{
            bankStatementResult = bankStatementService.getBankStatementData(accountNumber,fromDate,numberOfMonth);
        } catch (Exception e){
            log.error("Exception while get bankStatement data!", e);
            throw new DWHInterfaceException(e, ExceptionMapping.DWH_BANK_STATEMENT_EXCEPTION,msg.get(ExceptionMapping.DWH_BANK_STATEMENT_EXCEPTION,userId));
        }
        return bankStatementResult;
    }
}
