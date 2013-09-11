package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.db.master.SettlementStatus;
import com.clevel.selos.model.db.master.TDRCondition;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 6/9/2556
 * Time: 15:31 น.
 * To change this template use File | Settings | File Templates.
 */
public class NcbRecordView {

    private Boolean isTMBAccount;
    private String dateOfInfo;
    private String accountOpenDate;
    private BigDecimal limit;
    private BigDecimal outstanding;
    private BigDecimal installment;
    private String dateOfDebtRestructuring;
    private String typeOfCurrentPayment;
    private String typeOfHistoryPayment;
    private String noOfOutstandingPaymentIn12months;
    private String noOfOverLimit;
    private Boolean refinanceFlag;
    private String noOfmonthsPayment;
    private boolean monthsPaymentFlag;


    private AccountType accountType;
    private AccountStatus accountStatus;
    private SettlementStatus settlementStatus;
    private TDRCondition currentPayment;
    private TDRCondition historyPayment;

    public NcbRecordView(){

    }

    public boolean isMonthsPaymentFlag() {
        return monthsPaymentFlag;
    }

    public void setMonthsPaymentFlag(boolean monthsPaymentFlag) {
        this.monthsPaymentFlag = monthsPaymentFlag;
    }

    public Boolean getTMBAccount() {
        return isTMBAccount;
    }

    public void setTMBAccount(Boolean TMBAccount) {
        isTMBAccount = TMBAccount;
    }

    public String getDateOfInfo() {
        return dateOfInfo;
    }

    public void setDateOfInfo(String dateOfInfo) {
        this.dateOfInfo = dateOfInfo;
    }

    public String getAccountOpenDate() {
        return accountOpenDate;
    }

    public void setAccountOpenDate(String accountOpenDate) {
        this.accountOpenDate = accountOpenDate;
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

    public String getDateOfDebtRestructuring() {
        return dateOfDebtRestructuring;
    }

    public void setDateOfDebtRestructuring(String dateOfDebtRestructuring) {
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

    public Boolean getRefinanceFlag() {
        return refinanceFlag;
    }

    public void setRefinanceFlag(Boolean refinanceFlag) {
        this.refinanceFlag = refinanceFlag;
    }

    public String getNoOfmonthsPayment() {
        return noOfmonthsPayment;
    }

    public void setNoOfmonthsPayment(String noOfmonthsPayment) {
        this.noOfmonthsPayment = noOfmonthsPayment;
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

    public SettlementStatus getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(SettlementStatus settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public TDRCondition getCurrentPayment() {
        return currentPayment;
    }

    public void setCurrentPayment(TDRCondition currentPayment) {
        this.currentPayment = currentPayment;
    }

    public TDRCondition getHistoryPayment() {
        return historyPayment;
    }

    public void setHistoryPayment(TDRCondition historyPayment) {
        this.historyPayment = historyPayment;
    }
}
