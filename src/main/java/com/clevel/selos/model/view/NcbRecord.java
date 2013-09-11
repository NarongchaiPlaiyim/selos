package com.clevel.selos.model.view;

import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 6/9/2556
 * Time: 15:31 น.
 * To change this template use File | Settings | File Templates.
 */
public class NcbRecord {

    private String loanAccountType;
    private String loanAccountStatus;
    private String isTMBAccount;
    private String dateOfInfo;
    private String accountOpenDate;
    private String limit;
    private String outstanding;
    private String installment;
    private String dateOfDebtRestructuring;
    private String typeOfCurrentPayment;
    private String typeOfHistoryPayment;
    private String noOfOutstandingPaymentIn12months;
    private String noOfOverLimit;
    private String refinanceFlag;
    private String noOfmonthsPayment;


    private LinkedHashMap<String, String> isTMBAccounts;

    public LinkedHashMap<String, String> getTMBAccounts() {
        isTMBAccounts = new LinkedHashMap<String, String>();
        isTMBAccounts.put("Yes", "Y");
        isTMBAccounts.put("No", "N");
        return isTMBAccounts;
    }

    public NcbRecord(){
    }

    public String getLoanAccountType() {
        return loanAccountType;
    }

    public void setLoanAccountType(String loanAccountType) {
        this.loanAccountType = loanAccountType;
    }

    public String getLoanAccountStatus() {
        return loanAccountStatus;
    }

    public void setLoanAccountStatus(String loanAccountStatus) {
        this.loanAccountStatus = loanAccountStatus;
    }

    public String getTMBAccount() {
        return isTMBAccount;
    }

    public void setTMBAccount(String TMBAccount) {
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

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(String outstanding) {
        this.outstanding = outstanding;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
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

    public String getRefinanceFlag() {
        return refinanceFlag;
    }

    public void setRefinanceFlag(String refinanceFlag) {
        this.refinanceFlag = refinanceFlag;
    }

    public String getNoOfmonthsPayment() {
        return noOfmonthsPayment;
    }

    public void setNoOfmonthsPayment(String noOfmonthsPayment) {
        this.noOfmonthsPayment = noOfmonthsPayment;
    }
}
