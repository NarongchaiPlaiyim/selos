package com.clevel.selos.integration.dwh;

import com.clevel.selos.integration.DWH;
import com.clevel.selos.integration.DWHInterface;
import com.clevel.selos.integration.dwh.bankstatement.BankStatementService;
import com.clevel.selos.integration.dwh.bankstatement.model.BankStatement;
import com.clevel.selos.integration.dwh.obligation.model.Obligation;
import com.clevel.selos.integration.dwh.obligation.ObligationService;
import org.slf4j.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Default
public class DWHInterfaceImpl implements DWHInterface,Serializable{
    @Inject
    @DWH
    Logger log;

    @Inject
    ObligationService obligationService;
    @Inject
    BankStatementService bankStatementService;

    @Inject
    public DWHInterfaceImpl() {

    }


    @Override
    public List<Obligation> getObligationData(String userId, List<String> tmbCusIdList) {
        log.debug("getObligationData (userId : {}, tmbCusIdList : {})",userId,tmbCusIdList);
        List<Obligation> obligationList = Collections.EMPTY_LIST;
        if(tmbCusIdList!=null && tmbCusIdList.size()>0){
            obligationList = obligationService.getObligationByTmbCusId(tmbCusIdList);
            log.debug("getObligationData result (obligationList size : {})",obligationList.size());
        }
        return obligationList;
    }

    @Override
    public List<BankStatement> getBankStatementData(String userId, String accountNumber, Date fromDate, int numberOfMonth){
        log.debug("getBankStatementData (userId : {}, accountNumber : {}, fromDate : {}, numberOfMonth : {})",userId,accountNumber,fromDate,numberOfMonth);
        List<BankStatement> bankStatementList = Collections.EMPTY_LIST;
        bankStatementList = bankStatementService.getBankStatementData(accountNumber,fromDate,numberOfMonth);
        return bankStatementList;
    }
}
