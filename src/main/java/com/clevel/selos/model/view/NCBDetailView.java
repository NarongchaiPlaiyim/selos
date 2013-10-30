package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.db.master.SettlementStatus;
import com.clevel.selos.model.db.master.TDRCondition;
import com.clevel.selos.util.Util;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NCBDetailView implements Serializable {

    private long id;
    private int isTMBAccount;   // NCBBizTransform use
    private int refinanceFlag;
    private boolean monthsPaymentFlag;    // check render
    private int wcFlag;
    private Date dateOfInfo;
    private Date accountOpenDate;
    private BigDecimal limit;
    private BigDecimal outstanding;
    private BigDecimal installment;
    private Date dateOfDebtRestructuring;
    private BigDecimal noOfOutstandingPaymentIn12months;
    private int noOfOverLimit;
    private AccountType accountType;
    private AccountStatus accountStatus;
    private TDRCondition tdrCondition;
    private SettlementStatus currentPayment;
    private SettlementStatus historyPayment;
    private int noOfmonthsPayment;
    private String moneyTotal;
    private BigDecimal month1;
    private BigDecimal month2;
    private BigDecimal month3;
    private BigDecimal month4;
    private BigDecimal month5;
    private BigDecimal month6;
    private boolean canToEdit;
    private boolean monthFlagPage;

    private boolean TMB;
    private boolean refinance;
    private boolean wc;

    public NCBDetailView() {
        reset();
    }

    public void reset() {
        this.isTMBAccount = 0;
        this.dateOfInfo = new Date();
        this.accountOpenDate = new Date();
        this.limit = new BigDecimal(0);
        this.outstanding = new BigDecimal(0);
        this.installment = new BigDecimal(0);
        this.dateOfDebtRestructuring = new Date();
        this.noOfOutstandingPaymentIn12months = new BigDecimal(0);
        this.noOfOverLimit = 0;
        this.refinanceFlag = 0;
        this.monthsPaymentFlag = false;
        this.accountType = new AccountType();
        this.accountStatus = new AccountStatus();
        this.tdrCondition = new TDRCondition();
        this.currentPayment = new SettlementStatus();
        this.historyPayment = new SettlementStatus();
        this.noOfmonthsPayment = 0;
        this.moneyTotal = "";
        this.month1 = new BigDecimal(0);
        this.month2 = new BigDecimal(0);
        this.month3 = new BigDecimal(0);
        this.month4 = new BigDecimal(0);
        this.month5 = new BigDecimal(0);
        this.month6 = new BigDecimal(0);
        this.wcFlag = 0;
        this.canToEdit = false;
        this.monthFlagPage = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMoneyTotal() {
        String moneyTotal = "";
        List<BigDecimal> moneys;

        moneys = new ArrayList<BigDecimal>();
        moneys.add(this.month1);
        moneys.add(this.month2);
        moneys.add(this.month3);
        moneys.add(this.month4);
        moneys.add(this.month5);
        moneys.add(this.month6);

        if ((this.noOfmonthsPayment != 0)) {

            for (int i = 0; i < moneys.size(); i++) {
                if (i < this.noOfmonthsPayment) {
                    moneyTotal += " เดือนที่ " + (i + 1) + " : " + Util.formatNumber(moneys.get(i).doubleValue()) + " บาท ";
                }
            }

        }
        return moneyTotal;
    }

    public void setMoneyTotal(String moneyTotal) {
        this.moneyTotal = moneyTotal;
    }

    public int getNoOfmonthsPayment() {
        return noOfmonthsPayment;
    }

    public void setNoOfmonthsPayment(int noOfmonthsPayment) {
        this.noOfmonthsPayment = noOfmonthsPayment;
    }

    public Date getDateOfInfo() {
        return dateOfInfo;
    }

    public void setDateOfInfo(Date dateOfInfo) {
        this.dateOfInfo = dateOfInfo;
    }

    public int getTMBAccount() {
        if (TMB == true) {
            this.isTMBAccount = 1;
        } else {
            this.isTMBAccount = 0;
        }
        return isTMBAccount;
    }

    public void setTMBAccount(int TMBAccount) {
        isTMBAccount = TMBAccount;
    }

    public boolean isTMB() {

        if (isTMBAccount == 1) {
            this.TMB = true;
        } else {
            this.TMB = false;
        }
        return TMB;
    }

    public void setTMB(boolean TMB) {
        this.TMB = TMB;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(BigDecimal outstanding) {
        this.outstanding = outstanding;
    }

    public BigDecimal getInstallment() {
        return installment;
    }

    public void setInstallment(BigDecimal installment) {
        this.installment = installment;
    }

    public Date getAccountOpenDate() {
        return accountOpenDate;
    }

    public void setAccountOpenDate(Date accountOpenDate) {
        this.accountOpenDate = accountOpenDate;
    }

    public Date getDateOfDebtRestructuring() {
        return dateOfDebtRestructuring;
    }

    public void setDateOfDebtRestructuring(Date dateOfDebtRestructuring) {
        this.dateOfDebtRestructuring = dateOfDebtRestructuring;
    }

    public BigDecimal getNoOfOutstandingPaymentIn12months() {
        return noOfOutstandingPaymentIn12months;
    }

    public void setNoOfOutstandingPaymentIn12months(BigDecimal noOfOutstandingPaymentIn12months) {
        this.noOfOutstandingPaymentIn12months = noOfOutstandingPaymentIn12months;
    }

    public int getNoOfOverLimit() {
        return noOfOverLimit;
    }

    public void setNoOfOverLimit(int noOfOverLimit) {
        this.noOfOverLimit = noOfOverLimit;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }


    public TDRCondition getTdrCondition() {
        return tdrCondition;
    }

    public void setTdrCondition(TDRCondition tdrCondition) {
        this.tdrCondition = tdrCondition;
    }

    public SettlementStatus getCurrentPayment() {
        return currentPayment;
    }

    public void setCurrentPayment(SettlementStatus currentPayment) {
        this.currentPayment = currentPayment;
    }

    public SettlementStatus getHistoryPayment() {
        return historyPayment;
    }

    public void setHistoryPayment(SettlementStatus historyPayment) {
        this.historyPayment = historyPayment;
    }


    public BigDecimal getMonth1() {
        return month1;
    }

    public void setMonth1(BigDecimal month1) {
        this.month1 = month1;
    }

    public BigDecimal getMonth2() {
        return month2;
    }

    public void setMonth2(BigDecimal month2) {
        this.month2 = month2;
    }

    public BigDecimal getMonth3() {
        return month3;
    }

    public void setMonth3(BigDecimal month3) {
        this.month3 = month3;
    }

    public BigDecimal getMonth4() {
        return month4;
    }

    public void setMonth4(BigDecimal month4) {
        this.month4 = month4;
    }

    public BigDecimal getMonth5() {
        return month5;
    }

    public void setMonth5(BigDecimal month5) {
        this.month5 = month5;
    }

    public BigDecimal getMonth6() {
        return month6;
    }

    public void setMonth6(BigDecimal month6) {
        this.month6 = month6;
    }


    public boolean isRefinance() {
        if (refinanceFlag == 1) {
            this.refinance = true;
        } else {
            this.refinance = false;
        }
        return refinance;
    }

    public void setRefinance(boolean refinance) {
        this.refinance = refinance;
    }

    public boolean isWc() {
        if (wcFlag == 1) {
            this.wc = true;
        } else {
            this.wc = false;
        }
        return wc;
    }

    public void setWc(boolean wc) {
        this.wc = wc;
    }


    public int getRefinanceFlag() {
        if (refinance == true) {
            refinanceFlag = 1;
        } else {
            refinanceFlag = 0;
        }
        return refinanceFlag;
    }

    public void setRefinanceFlag(int refinanceFlag) {
        this.refinanceFlag = refinanceFlag;
    }


    public int getWcFlag() {
        if (wc == true) {
            wcFlag = 1;
        } else {
            wcFlag = 0;
        }
        return wcFlag;
    }

    public void setWcFlag(int wcFlag) {
        this.wcFlag = wcFlag;
    }

    public boolean isMonthsPaymentFlag() {
        if (this.noOfmonthsPayment != 0) {
            this.monthsPaymentFlag = true;
        } else {
            this.monthsPaymentFlag = false;
        }
        return monthsPaymentFlag;
    }

    public void setMonthsPaymentFlag(boolean monthsPaymentFlag) {
        this.monthsPaymentFlag = monthsPaymentFlag;
    }

    public boolean isCanToEdit() {
        return canToEdit;
    }

    public void setCanToEdit(boolean canToEdit) {
        this.canToEdit = canToEdit;
    }

    public boolean isMonthFlagPage() {
        if (accountType.getMonthFlag() == 1) {
            monthFlagPage = true;
        } else {
            monthFlagPage = false;
        }

        return monthFlagPage;
    }

    public void setMonthFlagPage(boolean monthFlagPage) {
        this.monthFlagPage = monthFlagPage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("isTMBAccount", isTMBAccount)
                .append("dateOfInfo", dateOfInfo)
                .append("accountOpenDate", accountOpenDate)
                .append("limit", limit)
                .append("outstanding", outstanding)
                .append("installment", installment)
                .append("dateOfDebtRestructuring", dateOfDebtRestructuring)
                .append("noOfOutstandingPaymentIn12months", noOfOutstandingPaymentIn12months)
                .append("noOfOverLimit", noOfOverLimit)
                .append("refinanceFlag", refinanceFlag)
                .append("monthsPaymentFlag", monthsPaymentFlag)
                .append("accountType", accountType)
                .append("accountStatus", accountStatus)
                .append("tdrCondition", tdrCondition)
                .append("currentPayment", currentPayment)
                .append("historyPayment", historyPayment)
                .append("noOfmonthsPayment", noOfmonthsPayment)
                .append("moneyTotal", moneyTotal)
                .append("month1", month1)
                .append("month2", month2)
                .append("month3", month3)
                .append("month4", month4)
                .append("month5", month5)
                .append("month6", month6)
                .append("wcFlag", wcFlag)
                .toString();
    }
}
