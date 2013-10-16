package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.db.master.SettlementStatus;
import com.clevel.selos.model.db.master.TDRCondition;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class NCBDetailView implements Serializable {

    private long id;
    private boolean isTMBAccount;
    private Date dateOfInfo;
    private Date accountOpenDate;
    private BigDecimal limit;
    private BigDecimal outstanding;
    private BigDecimal installment;
    private Date dateOfDebtRestructuring;
    private String typeOfCurrentPayment;
    private String typeOfHistoryPayment;
    private BigDecimal noOfOutstandingPaymentIn12months;
    private int noOfOverLimit;
    private boolean refinanceFlag;
    private boolean monthsPaymentFlag;
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
    private boolean wcFlag;

    public NCBDetailView() {
        reset();
    }

    public void reset() {
        this.isTMBAccount = false;
        this.dateOfInfo = new Date();
        this.accountOpenDate = new Date();
        this.limit = new BigDecimal(0);
        this.outstanding = new BigDecimal(0);
        this.installment = new BigDecimal(0);
        this.dateOfDebtRestructuring = new Date();
        this.typeOfCurrentPayment = "";
        this.typeOfHistoryPayment = "";
        this.noOfOutstandingPaymentIn12months = new BigDecimal(0);
        this.noOfOverLimit = 0;
        this.refinanceFlag = false;
        this.monthsPaymentFlag = false;
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
        this.wcFlag = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMoneyTotal() {
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

    public boolean isMonthsPaymentFlag() {
        return monthsPaymentFlag;
    }

    public void setMonthsPaymentFlag(boolean monthsPaymentFlag) {
        this.monthsPaymentFlag = monthsPaymentFlag;
    }

    public Date getDateOfInfo() {
        return dateOfInfo;
    }

    public void setDateOfInfo(Date dateOfInfo) {
        this.dateOfInfo = dateOfInfo;
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

    public String getTypeOfCurrentPayment() {
        return typeOfCurrentPayment;
    }

    public void setTypeOfCurrentPayment(String typeOfCurrentPayment) {
        this.typeOfCurrentPayment = typeOfCurrentPayment;
    }

    public String getTypeOfHistoryPayment() {
        return typeOfHistoryPayment;
    }

    public void setTypeOfHistoryPayment(String typeOfHistoryPayment) {
        this.typeOfHistoryPayment = typeOfHistoryPayment;
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

    public boolean isTMBAccount() {
        return isTMBAccount;
    }

    public void setTMBAccount(boolean TMBAccount) {
        isTMBAccount = TMBAccount;
    }

    public boolean isRefinanceFlag() {
        return refinanceFlag;
    }

    public void setRefinanceFlag(boolean refinanceFlag) {
        this.refinanceFlag = refinanceFlag;
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

    public boolean isWcFlag() {
        return wcFlag;
    }

    public void setWcFlag(boolean wcFlag) {
        this.wcFlag = wcFlag;
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
                .append("typeOfCurrentPayment", typeOfCurrentPayment)
                .append("typeOfHistoryPayment", typeOfHistoryPayment)
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