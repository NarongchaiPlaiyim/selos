package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.db.master.SettlementStatus;
import com.clevel.selos.model.db.master.TDRCondition;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 6/9/2556
 * Time: 15:31 น.
 * To change this template use File | Settings | File Templates.
 */
public class NcbRecordView implements Serializable {


    private String isTMBAccount;
    private Date dateOfInfo;
    private Date accountOpenDate;
    private BigDecimal limit;
    private BigDecimal outstanding;
    private BigDecimal installment;
    private Date dateOfDebtRestructuring;
    private String typeOfCurrentPayment;
    private String typeOfHistoryPayment;
    private String noOfOutstandingPaymentIn12months;
    private String noOfOverLimit;
    private String refinanceFlag;
    private boolean monthsPaymentFlag;
    private AccountType accountType;
    private AccountStatus accountStatus;
    private TDRCondition tdrCondition;
    private SettlementStatus currentPayment;
    private SettlementStatus historyPayment;
    private List<MonthsPaymentView> noOfmonthsPaymentList;
    private int noOfmonthsPayment;
    private List<MoneyPaymentView> moneyPaymentViewList;
    private String moneyTotal;
    private BigDecimal month1;
    private BigDecimal month2;
    private BigDecimal month3;
    private BigDecimal month4;
    private BigDecimal month5;
    private BigDecimal month6;

    public NcbRecordView() {
        reset();
    }

    public void reset() {
        this.isTMBAccount = "";
        this.dateOfInfo = new Date();
        this.accountOpenDate = new Date();
        this.limit = new BigDecimal(0);
        this.outstanding = new BigDecimal(0);
        this.installment = new BigDecimal(0);
        this.dateOfDebtRestructuring = new Date();
        this.typeOfCurrentPayment = "";
        this.typeOfHistoryPayment = "";
        this.noOfOutstandingPaymentIn12months = "";
        this.noOfOverLimit = "";
        this.refinanceFlag = "";
        this.monthsPaymentFlag = true;
        this.accountType = new AccountType();
        this.accountStatus = new AccountStatus();
        this.tdrCondition = new TDRCondition();
        this.currentPayment = new SettlementStatus();
        this.historyPayment = new SettlementStatus();
        this.noOfmonthsPaymentList = new ArrayList<MonthsPaymentView>();
        this.noOfmonthsPayment = 0;
        this.moneyPaymentViewList = new ArrayList<MoneyPaymentView>();
        this.moneyTotal = "";
        this.month1 = new BigDecimal(0);
        this.month2 = new BigDecimal(0);
        this.month3 = new BigDecimal(0);
        this.month4 = new BigDecimal(0);
        this.month5 = new BigDecimal(0);
        this.month6 = new BigDecimal(0);


        noOfmonthsPaymentList = new ArrayList<MonthsPaymentView>();
        for (int i = 1; i < 7; i++) {
            noOfmonthsPaymentList.add(new MonthsPaymentView(i));
        }

        moneyPaymentViewList = new ArrayList<MoneyPaymentView>();


    }

    public String getMoneyTotal() {
        return moneyTotal;
    }

    public void setMoneyTotal(String moneyTotal) {
        this.moneyTotal = moneyTotal;
    }

    public List<MoneyPaymentView> getMoneyPaymentViewList() {
        return moneyPaymentViewList;
    }

    public void setMoneyPaymentViewList(List<MoneyPaymentView> moneyPaymentViewList) {
        this.moneyPaymentViewList = moneyPaymentViewList;
    }

    public List<MonthsPaymentView> getNoOfmonthsPaymentList() {
        return noOfmonthsPaymentList;
    }

    public void setNoOfmonthsPaymentList(List<MonthsPaymentView> noOfmonthsPaymentList) {
        this.noOfmonthsPaymentList = noOfmonthsPaymentList;
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

    public String getNoOfOutstandingPaymentIn12months() {
        return noOfOutstandingPaymentIn12months;
    }

    public void setNoOfOutstandingPaymentIn12months(String noOfOutstandingPaymentIn12months) {
        this.noOfOutstandingPaymentIn12months = noOfOutstandingPaymentIn12months;
    }

    public String getNoOfOverLimit() {
        return noOfOverLimit;
    }

    public void setNoOfOverLimit(String noOfOverLimit) {
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

    public String getTMBAccount() {
        return isTMBAccount;
    }

    public void setTMBAccount(String TMBAccount) {
        isTMBAccount = TMBAccount;
    }

    public String getRefinanceFlag() {
        return refinanceFlag;
    }

    public void setRefinanceFlag(String refinanceFlag) {
        this.refinanceFlag = refinanceFlag;
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


}
