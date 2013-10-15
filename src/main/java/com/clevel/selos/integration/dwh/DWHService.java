package com.clevel.selos.integration.dwh;

import com.clevel.selos.dao.ext.Obligation1DAO;
import com.clevel.selos.dao.ext.Obligation2DAO;
import com.clevel.selos.dao.system.SystemParameterDAO;
import com.clevel.selos.integration.DWH;
import com.clevel.selos.integration.dwh.model.Obligation;
import com.clevel.selos.model.db.ext.Obligation1;
import com.clevel.selos.model.db.ext.Obligation2;
import com.clevel.selos.model.db.system.SystemParameter;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DWHService implements Serializable {
    @Inject
    @DWH
    Logger log;

    @Inject
    SystemParameterDAO systemParameterDAO;
    @Inject
    Obligation1DAO obligation1DAO;
    @Inject
    Obligation2DAO obligation2DAO;

    @Inject
    public DWHService() {

    }

    public List<Obligation> getObligationByTmbCusId(String tmbCusId){
        log.debug("getObligationByTmbCusId (tmbCusId : {})",tmbCusId);
        List<Obligation> obligationList = new ArrayList<Obligation>();

        //check which table is current
        SystemParameter systemParameter = systemParameterDAO.findByParameterName("EXT_DWH_OBLIGATION");

        if(systemParameter!=null){
            String value = systemParameter.getValue();

            if(value.equalsIgnoreCase("1")){
                //get from table 1 (Obligation1)
                List<Obligation1> obligation1List = obligation1DAO.getListByTmbCusId(tmbCusId);
                if(obligation1List!=null && obligation1List.size()>0){
                    return transformObligation(obligation1List,null);
                }
            } else if(value.equalsIgnoreCase("2")){
                //get from table 2 (Obligation2)
                List<Obligation2> obligation2List = obligation2DAO.getListByTmbCusId(tmbCusId);
                if(obligation2List!=null && obligation2List.size()>0){
                    return transformObligation(null,obligation2List);
                }
            }
        }

        return obligationList;
    }

    public List<Obligation> transformObligation(List<Obligation1> obligation1List, List<Obligation2> obligation2List){
        List<Obligation> obligationList = new ArrayList<Obligation>();
        if(obligation1List!=null){
            for(Obligation1 obligation1:obligation1List){
                Obligation obligation = new Obligation();
                obligation.setId(obligation1.getId());
                obligation.setTmbCusId(obligation1.getTmbCusId());
                obligation.setTmbBotCusId(obligation1.getTmbBotCusId());
                obligation.setServiceSegment(obligation1.getServiceSegment());
                obligation.setAdjustClass(obligation1.getAdjustClass());
                obligation.setLastContractDate(obligation1.getLastContractDate());
                obligation.setMisProductGroup(obligation1.getMisProductGroup());
                obligation.setAccountServiceSegment(obligation1.getAccountServiceSegment());
                obligation.setProductCode(obligation1.getProductCode());
                obligation.setProjectCode(obligation1.getProjectCode());
                obligation.setDataSource(obligation1.getDataSource());
                obligation.setAccountName(obligation1.getAccountName());
                obligation.setLastReviewDate(obligation1.getLastReviewDate());
                obligation.setNextReviewDate(obligation1.getNextReviewDate());
                obligation.setExtendedReviewDate(obligation1.getExtendedReviewDate());
                obligation.setScfScoreFinalRate(obligation1.getScfScoreFinalRate());
                obligation.setScfScoreMsFinal(obligation1.getScfScoreMsFinal());
                obligation.setScfScoreModelTypeIbnr(obligation1.getScfScoreModelTypeIbnr());
                obligation.setClaimAmount(obligation1.getClaimAmount());
                obligation.setComAmount(obligation1.getComAmount());
                obligation.setTmbPaidExpenseAmount(obligation1.getTmbPaidExpenseAmount());
                obligation.setIntUnEarned(obligation1.getIntUnEarned());
                obligation.setIntAccrued(obligation1.getIntAccrued());
                obligation.setTmbBotCommitment(obligation1.getTmbBotCommitment());
                obligation.setCurrBookBal(obligation1.getCurrBookBal());
                obligation.setTenors(obligation1.getTenors());
                obligation.setAccountNumber(obligation1.getAccountNumber());
                obligation.setAccountSuffix(obligation1.getAccountSuffix());
                obligation.setAccountRef(obligation1.getAccountRef());
                obligation.setAccountStatus(obligation1.getAccountStatus());
                obligation.setCardBlockCode(obligation1.getCardBlockCode());
                obligation.setCusRelAccount(obligation1.getCusRelAccount());
                obligation.setTdrFlag(obligation1.getTdrFlag());
                obligation.setNumMonthIntPastDue(obligation1.getNumMonthIntPastDue());
                obligation.setNumMonthIntPastDueTDRAcc(obligation1.getNumMonthIntPastDueTDRAcc());
                obligationList.add(obligation);
            }
        } else if(obligation2List!=null){
            for(Obligation2 obligation2:obligation2List){
                Obligation obligation = new Obligation();
                obligation.setId(obligation2.getId());
                obligation.setTmbCusId(obligation2.getTmbCusId());
                obligation.setTmbBotCusId(obligation2.getTmbBotCusId());
                obligation.setServiceSegment(obligation2.getServiceSegment());
                obligation.setAdjustClass(obligation2.getAdjustClass());
                obligation.setLastContractDate(obligation2.getLastContractDate());
                obligation.setMisProductGroup(obligation2.getMisProductGroup());
                obligation.setAccountServiceSegment(obligation2.getAccountServiceSegment());
                obligation.setProductCode(obligation2.getProductCode());
                obligation.setProjectCode(obligation2.getProjectCode());
                obligation.setDataSource(obligation2.getDataSource());
                obligation.setAccountName(obligation2.getAccountName());
                obligation.setLastReviewDate(obligation2.getLastReviewDate());
                obligation.setNextReviewDate(obligation2.getNextReviewDate());
                obligation.setExtendedReviewDate(obligation2.getExtendedReviewDate());
                obligation.setScfScoreFinalRate(obligation2.getScfScoreFinalRate());
                obligation.setScfScoreMsFinal(obligation2.getScfScoreMsFinal());
                obligation.setScfScoreModelTypeIbnr(obligation2.getScfScoreModelTypeIbnr());
                obligation.setClaimAmount(obligation2.getClaimAmount());
                obligation.setComAmount(obligation2.getComAmount());
                obligation.setTmbPaidExpenseAmount(obligation2.getTmbPaidExpenseAmount());
                obligation.setIntUnEarned(obligation2.getIntUnEarned());
                obligation.setIntAccrued(obligation2.getIntAccrued());
                obligation.setTmbBotCommitment(obligation2.getTmbBotCommitment());
                obligation.setCurrBookBal(obligation2.getCurrBookBal());
                obligation.setTenors(obligation2.getTenors());
                obligation.setAccountNumber(obligation2.getAccountNumber());
                obligation.setAccountSuffix(obligation2.getAccountSuffix());
                obligation.setAccountRef(obligation2.getAccountRef());
                obligation.setAccountStatus(obligation2.getAccountStatus());
                obligation.setCardBlockCode(obligation2.getCardBlockCode());
                obligation.setCusRelAccount(obligation2.getCusRelAccount());
                obligation.setTdrFlag(obligation2.getTdrFlag());
                obligation.setNumMonthIntPastDue(obligation2.getNumMonthIntPastDue());
                obligation.setNumMonthIntPastDueTDRAcc(obligation2.getNumMonthIntPastDueTDRAcc());
                obligationList.add(obligation);
            }
        }

        return obligationList;
    }
}
