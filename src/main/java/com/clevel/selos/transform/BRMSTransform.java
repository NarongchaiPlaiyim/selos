package com.clevel.selos.transform;

import com.clevel.selos.controller.PrescreenResult;
import com.clevel.selos.integration.brms.model.request.BRMSAccountStmtInfo;
import com.clevel.selos.integration.brms.model.request.BRMSBizInfo;
import com.clevel.selos.integration.brms.model.request.BRMSNCBAccountInfo;
import com.clevel.selos.integration.brms.model.response.UWRulesResponse;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.working.BankStatement;
import com.clevel.selos.model.db.working.NCBDetail;
import com.clevel.selos.util.DateTimeUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;

public class BRMSTransform extends Transform{

    @Inject
    private Logger logger;

    @Inject
    public BRMSTransform(){

    }

    public BRMSAccountStmtInfo getBRMSBizInfo(BankStatement bankStatement){
        logger.debug("transform BankStatement {}", bankStatement);
        BRMSAccountStmtInfo accountStmtInfo = new BRMSAccountStmtInfo();
        accountStmtInfo.setAvgUtilizationPercent(bankStatement.getAvgUtilizationPercent());
        accountStmtInfo.setAvgSwingPercent(bankStatement.getAvgSwingPercent());
        accountStmtInfo.setAvgIncomeGross(bankStatement.getAvgIncomeGross());
        accountStmtInfo.setTotalTransaction(bankStatement.getTotalTransaction());
        accountStmtInfo.setMainAccount(getRadioBoolean(bankStatement.getMainAccount()));
        accountStmtInfo.setHighestInflow(toBoolean(bankStatement.getHighestInflow()));
        accountStmtInfo.setTmb(toBoolean(bankStatement.getTMB()));
        accountStmtInfo.setNotCountIncome(isActive(bankStatement.getNotCountIncome()));
        logger.debug("transform Result {}", accountStmtInfo);
        return accountStmtInfo;
    }

    public BRMSNCBAccountInfo getBRMSNCBAccountInfo(NCBDetail ncbDetail, boolean isIndividual){
        BRMSNCBAccountInfo ncbAccountInfo = new BRMSNCBAccountInfo();
        Date now = Calendar.getInstance().getTime();
        if(isIndividual)
            ncbAccountInfo.setLoanAccountStatus(ncbDetail.getAccountStatus().getNcbCodeInd());
        else
            ncbAccountInfo.setLoanAccountStatus(ncbDetail.getAccountStatus().getNcbCodeJur());
        ncbAccountInfo.setLoanAccountType(ncbDetail.getAccountType().getNcbCode());
        ncbAccountInfo.setTmbFlag(isActive(ncbDetail.getAccountTMBFlag()));
        ncbAccountInfo.setNplFlag(isActive(ncbDetail.getNplFlag()));
        ncbAccountInfo.setCreditAmtAtNPLDate(ncbDetail.getNplCreditAmount());
        ncbAccountInfo.setTdrFlag(isActive(ncbDetail.getTdrFlag()));
        ncbAccountInfo.setCurrentPaymentType(ncbDetail.getCurrentPayment().getNcbCode());
        ncbAccountInfo.setSixMonthPaymentType(ncbDetail.getHistorySixPayment().getNcbCode());
        ncbAccountInfo.setTwelveMonthPaymentType(ncbDetail.getHistoryTwelvePayment().getNcbCode());
        ncbAccountInfo.setNumberOfOverDue(ncbDetail.getOutstandingIn12Month());
        ncbAccountInfo.setNumberOfOverLimit(ncbDetail.getOverLimit());
        if(ncbDetail.getAccountCloseDate() != null)
            ncbAccountInfo.setAccountCloseDateMonths(String.valueOf(DateTimeUtil.monthBetween2DatesWithNoDate(ncbDetail.getAccountCloseDate(), now)));
        else
            ncbAccountInfo.setAccountCloseDateMonths(String.valueOf(0));
        return ncbAccountInfo;
    }

    private boolean getRadioBoolean(int value){
        RadioValue radioValue = RadioValue.lookup(value);
        return radioValue.getBoolValue();
    }

    private boolean isActive(int value){
        return value == 1;
    }

    private boolean toBoolean(String value){
        return "Y".equals(value)? true: false;
    }

}
