package com.clevel.selos.integration.dwh.obligation;

import com.clevel.selos.dao.ext.dwh.Obligation1DAO;
import com.clevel.selos.dao.ext.dwh.Obligation2DAO;
import com.clevel.selos.dao.system.SystemParameterDAO;
import com.clevel.selos.integration.DWH;
import com.clevel.selos.integration.dwh.obligation.model.Obligation;
import com.clevel.selos.integration.dwh.obligation.model.ObligationResult;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.ext.dwh.Obligation1;
import com.clevel.selos.model.db.ext.dwh.Obligation2;
import com.clevel.selos.model.db.system.SystemParameter;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ObligationService implements Serializable {
    @Inject
    @DWH
    Logger log;

    @Inject
    @Config(name = "interface.dwh.obligation.table.sysparam")
    String sysParam;

    @Inject
    SystemParameterDAO systemParameterDAO;
    @Inject
    Obligation1DAO obligation1DAO;
    @Inject
    Obligation2DAO obligation2DAO;

    @Inject
    @ExceptionMessage
    Message exceptionMsg;

    @Inject
    public ObligationService() {

    }

    public ObligationResult getObligationByTmbCusId(List<String> tmbCusIdList) {
        log.debug("getObligationByTmbCusId (tmbCusIdList : {})", tmbCusIdList);
        ObligationResult obligationResult = new ObligationResult();

        //check which table is current
        SystemParameter systemParameter = systemParameterDAO.findByParameterName(sysParam);

        if (systemParameter != null) {
            String value = systemParameter.getValue();

            if (value.equalsIgnoreCase("1")) {
                //get from table 1 (Obligation1)
                List<Obligation1> obligation1List = obligation1DAO.getListByTmbCusIdList(tmbCusIdList);
                if (obligation1List != null && obligation1List.size() > 0) {
                    List<Obligation> obligationList = transformObligation(obligation1List, null);
                    obligationResult.setActionResult(ActionResult.SUCCESS);
                    obligationResult.setObligationList(obligationList);
                } else {
                    obligationResult.setActionResult(ActionResult.FAILED);
                    obligationResult.setReason(exceptionMsg.get(ExceptionMapping.DWH_DATA_NOT_FOUND));
                    obligationResult.setObligationList(new ArrayList<Obligation>());
                }
            } else if (value.equalsIgnoreCase("2")) {
                //get from table 2 (Obligation2)
                List<Obligation2> obligation2List = obligation2DAO.getListByTmbCusIdList(tmbCusIdList);
                if (obligation2List != null && obligation2List.size() > 0) {
                    List<Obligation> obligationList = transformObligation(null, obligation2List);
                    obligationResult.setActionResult(ActionResult.SUCCESS);
                    obligationResult.setObligationList(obligationList);
                } else {
                    obligationResult.setActionResult(ActionResult.FAILED);
                    obligationResult.setReason(exceptionMsg.get(ExceptionMapping.DWH_DATA_NOT_FOUND));
                    obligationResult.setObligationList(new ArrayList<Obligation>());
                }
            } else {
                obligationResult.setActionResult(ActionResult.FAILED);
                obligationResult.setReason(exceptionMsg.get(ExceptionMapping.INVALID_SYSTEM_PARAM));
                obligationResult.setObligationList(new ArrayList<Obligation>());
            }
        } else {
            obligationResult.setActionResult(ActionResult.FAILED);
            obligationResult.setReason(exceptionMsg.get(ExceptionMapping.NOT_FOUND_SYSTEM_PARAM));
            obligationResult.setObligationList(new ArrayList<Obligation>());
        }

        return obligationResult;
    }

    public ObligationResult getObligationByTmbCusId(String tmbCusId) {
        log.debug("getObligationByTmbCusId (tmbCusId : {})", tmbCusId);
        ObligationResult obligationResult = new ObligationResult();

        //check which table is current
        SystemParameter systemParameter = systemParameterDAO.findByParameterName(sysParam);

        if (systemParameter != null) {
            String value = systemParameter.getValue();

            if (value.equalsIgnoreCase("1")) {
                //get from table 1 (Obligation1)
                List<Obligation1> obligation1List = obligation1DAO.getListByTmbCusId(tmbCusId);
                if (obligation1List != null && obligation1List.size() > 0) {
                    List<Obligation> obligationList = transformObligation(obligation1List, null);
                    obligationResult.setActionResult(ActionResult.SUCCESS);
                    obligationResult.setObligationList(obligationList);
                } else {
                    obligationResult.setActionResult(ActionResult.FAILED);
                    obligationResult.setReason(exceptionMsg.get(ExceptionMapping.DWH_DATA_NOT_FOUND));
                    obligationResult.setObligationList(new ArrayList<Obligation>());
                }
            } else if (value.equalsIgnoreCase("2")) {
                //get from table 2 (Obligation2)
                List<Obligation2> obligation2List = obligation2DAO.getListByTmbCusId(tmbCusId);
                if (obligation2List != null && obligation2List.size() > 0) {
                    List<Obligation> obligationList = transformObligation(null, obligation2List);
                    obligationResult.setActionResult(ActionResult.SUCCESS);
                    obligationResult.setObligationList(obligationList);
                } else {
                    obligationResult.setActionResult(ActionResult.FAILED);
                    obligationResult.setReason(exceptionMsg.get(ExceptionMapping.DWH_DATA_NOT_FOUND));
                    obligationResult.setObligationList(new ArrayList<Obligation>());
                }
            } else {
                obligationResult.setActionResult(ActionResult.FAILED);
                obligationResult.setReason(exceptionMsg.get(ExceptionMapping.INVALID_SYSTEM_PARAM));
                obligationResult.setObligationList(new ArrayList<Obligation>());
            }
        } else {
            obligationResult.setActionResult(ActionResult.FAILED);
            obligationResult.setReason(exceptionMsg.get(ExceptionMapping.NOT_FOUND_SYSTEM_PARAM));
            obligationResult.setObligationList(new ArrayList<Obligation>());
        }

        return obligationResult;
    }

    public List<Obligation> transformObligation(List<Obligation1> obligation1List, List<Obligation2> obligation2List) {
        List<Obligation> obligationList = new ArrayList<Obligation>();
        if (obligation1List != null) {
            for (Obligation1 obligation1 : obligation1List) {
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
                obligation.setTmbIntUnEarned(obligation1.getIntUnEarned());
                obligation.setTmbIntAccrued(obligation1.getIntAccrued());
                obligation.setLimit(obligation1.getLimit());
                obligation.setOutstanding(obligation1.getOutstanding());
                obligation.setAccountNumber(obligation1.getAccountNumber());
                obligation.setAccountSuffix(obligation1.getAccountSuffix());
                obligation.setAccountRef(obligation1.getAccountRef());
                obligation.setAccountStatus(obligation1.getAccountStatus());
                obligation.setCardBlockCode(obligation1.getCardBlockCode());
                obligation.setCusRelAccount(obligation1.getCusRelAccount());
                obligation.setTdrFlag(obligation1.getTdrFlag());
                obligation.setNumMonthIntPastDue(obligation1.getNumMonthIntPastDue());
                obligation.setNumMonthIntPastDueTDRAcc(obligation1.getNumMonthIntPastDueTDRAcc());
                obligation.setTmbDelPriDay(obligation1.getTmbDelPriDay());
                obligation.setTmbDelIntDay(obligation1.getTmbDelIntDay());
                obligation.setTmbInstallmentAmt(obligation1.getTmbInstallmentAmt());
                obligation.setCovenantFlag(obligation1.getCovenantFlag());
                obligation.setReviewFlag(obligation1.getReviewFlag());
                obligation.setTmbUnusedBalance(obligation1.getTmbUnusedBalance());
                obligation.setTmbExtProductTypeCD(obligation1.getTmbExtProductTypeCD());
                obligation.setTmbTypeCRD(obligation1.getTmbTypeCRD());
                obligation.setTmbRMRef(obligation1.getTmbRMRef());
                obligation.setMaturityDate(obligation1.getMaturityDate());
                obligationList.add(obligation);
            }
        } else if (obligation2List != null) {
            for (Obligation2 obligation2 : obligation2List) {
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
                obligation.setTmbIntUnEarned(obligation2.getIntUnEarned());
                obligation.setTmbIntAccrued(obligation2.getIntAccrued());
                obligation.setLimit(obligation2.getLimit());
                obligation.setOutstanding(obligation2.getOutstanding());
                obligation.setAccountNumber(obligation2.getAccountNumber());
                obligation.setAccountSuffix(obligation2.getAccountSuffix());
                obligation.setAccountRef(obligation2.getAccountRef());
                obligation.setAccountStatus(obligation2.getAccountStatus());
                obligation.setCardBlockCode(obligation2.getCardBlockCode());
                obligation.setCusRelAccount(obligation2.getCusRelAccount());
                obligation.setTdrFlag(obligation2.getTdrFlag());
                obligation.setNumMonthIntPastDue(obligation2.getNumMonthIntPastDue());
                obligation.setNumMonthIntPastDueTDRAcc(obligation2.getNumMonthIntPastDueTDRAcc());
                obligation.setTmbDelPriDay(obligation2.getTmbDelPriDay());
                obligation.setTmbDelIntDay(obligation2.getTmbDelIntDay());
                obligation.setTmbInstallmentAmt(obligation2.getTmbInstallmentAmt());
                obligation.setCovenantFlag(obligation2.getCovenantFlag());
                obligation.setReviewFlag(obligation2.getReviewFlag());
                obligation.setTmbUnusedBalance(obligation2.getTmbUnusedBalance());
                obligation.setTmbExtProductTypeCD(obligation2.getTmbExtProductTypeCD());
                obligation.setTmbTypeCRD(obligation2.getTmbTypeCRD());
                obligation.setTmbRMRef(obligation2.getTmbRMRef());
                obligation.setMaturityDate(obligation2.getMaturityDate());
                obligationList.add(obligation);
            }
        }

        return obligationList;
    }
}
