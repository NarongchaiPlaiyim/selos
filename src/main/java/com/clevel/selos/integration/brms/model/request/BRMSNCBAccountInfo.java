package com.clevel.selos.integration.brms.model.request;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class BRMSNCBAccountInfo implements Serializable{
    private String loanAccountStatus;
    private String loanAccountType;
    private boolean tmbFlag;
    private boolean nplFlag;
    private BigDecimal creditAmtAtNPLDate;
    private boolean tdrFlag;
    private int customerEntity;
    private String currentPaymentType;
    private String sixMonthPaymentType; //Payment Pattern in last 6 months.
    private String twelveMonthPaymentType; //Payment Pattern in last 12 months.
    private int numberOfOverDue;//Over Due 31-60 days within last 12 months.
    private int numberOfOverLimit;//Number of Over Limit last 6 months.
    private BigDecimal accountCloseDateMonths; //Number of month from account close date;

    public String getLoanAccountStatus() {
        return loanAccountStatus;
    }

    public void setLoanAccountStatus(String loanAccountStatus) {
        this.loanAccountStatus = loanAccountStatus;
    }

    public String getLoanAccountType() {
        return loanAccountType;
    }

    public void setLoanAccountType(String loanAccountType) {
        this.loanAccountType = loanAccountType;
    }

    public boolean isTmbFlag() {
        return tmbFlag;
    }

    public void setTmbFlag(boolean tmbFlag) {
        this.tmbFlag = tmbFlag;
    }

    public boolean isNplFlag() {
        return nplFlag;
    }

    public void setNplFlag(boolean nplFlag) {
        this.nplFlag = nplFlag;
    }

    public BigDecimal getCreditAmtAtNPLDate() {
        return creditAmtAtNPLDate;
    }

    public void setCreditAmtAtNPLDate(BigDecimal creditAmtAtNPLDate) {
        this.creditAmtAtNPLDate = creditAmtAtNPLDate;
    }

    public boolean isTdrFlag() {
        return tdrFlag;
    }

    public void setTdrFlag(boolean tdrFlag) {
        this.tdrFlag = tdrFlag;
    }

    public int getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(int customerEntity) {
        this.customerEntity = customerEntity;
    }

    public String getCurrentPaymentType() {
        return currentPaymentType;
    }

    public void setCurrentPaymentType(String currentPaymentType) {
        this.currentPaymentType = currentPaymentType;
    }

    public String getSixMonthPaymentType() {
        return sixMonthPaymentType;
    }

    public void setSixMonthPaymentType(String sixMonthPaymentType) {
        this.sixMonthPaymentType = sixMonthPaymentType;
    }

    public String getTwelveMonthPaymentType() {
        return twelveMonthPaymentType;
    }

    public void setTwelveMonthPaymentType(String twelveMonthPaymentType) {
        this.twelveMonthPaymentType = twelveMonthPaymentType;
    }

    public int getNumberOfOverDue() {
        return numberOfOverDue;
    }

    public void setNumberOfOverDue(int numberOfOverDue) {
        this.numberOfOverDue = numberOfOverDue;
    }

    public int getNumberOfOverLimit() {
        return numberOfOverLimit;
    }

    public void setNumberOfOverLimit(int numberOfOverLimit) {
        this.numberOfOverLimit = numberOfOverLimit;
    }

    public BigDecimal getAccountCloseDateMonths() {
        return accountCloseDateMonths;
    }

    public void setAccountCloseDateMonths(BigDecimal accountCloseDateMonths) {
        this.accountCloseDateMonths = accountCloseDateMonths;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("loanAccountStatus", loanAccountStatus)
                .append("loanAccountType", loanAccountType)
                .append("tmbFlag", tmbFlag)
                .append("nplFlag", nplFlag)
                .append("creditAmtAtNPLDate", creditAmtAtNPLDate)
                .append("tdrFlag", tdrFlag)
                .append("customerEntity", customerEntity)
                .append("currentPaymentType", currentPaymentType)
                .append("sixMonthPaymentType", sixMonthPaymentType)
                .append("twelveMonthPaymentType", twelveMonthPaymentType)
                .append("numberOfOverDue", numberOfOverDue)
                .append("numberOfOverLimit", numberOfOverLimit)
                .append("accountCloseDateMonths", accountCloseDateMonths)
                .toString();
    }
}
