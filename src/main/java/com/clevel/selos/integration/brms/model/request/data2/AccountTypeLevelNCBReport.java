package com.clevel.selos.integration.brms.model.request.data2;

import com.clevel.selos.model.db.master.SettlementStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountTypeLevelNCBReport implements Serializable {
    private String ncbAccountStatus; //todo : to be enum
    private BigDecimal creditAmountFirstNPL;
    private boolean ncbTDRFlag;
    private SettlementStatus currentPayment;
    private SettlementStatus paymentPattern6M;
    private SettlementStatus paymentPattern12M;
    private int overDue31to60DaysWithinLast12Months;
    private int overLimitWithinLast6Months;
    private AttributeTypeLevelAccountTypeNCB attributeType;
    public AccountTypeLevelNCBReport() {
    }

    public AccountTypeLevelNCBReport(String ncbAccountStatus, BigDecimal creditAmountFirstNPL, boolean ncbTDRFlag, SettlementStatus currentPayment, SettlementStatus paymentPattern6M, SettlementStatus paymentPattern12M, int overDue31to60DaysWithinLast12Months, int overLimitWithinLast6Months, AttributeTypeLevelAccountTypeNCB attributeType) {
        this.ncbAccountStatus = ncbAccountStatus;
        this.creditAmountFirstNPL = creditAmountFirstNPL;
        this.ncbTDRFlag = ncbTDRFlag;
        this.currentPayment = currentPayment;
        this.paymentPattern6M = paymentPattern6M;
        this.paymentPattern12M = paymentPattern12M;
        this.overDue31to60DaysWithinLast12Months = overDue31to60DaysWithinLast12Months;
        this.overLimitWithinLast6Months = overLimitWithinLast6Months;
        this.attributeType = attributeType;
    }

    public String getNcbAccountStatus() {
        return ncbAccountStatus;
    }

    public void setNcbAccountStatus(String ncbAccountStatus) {
        this.ncbAccountStatus = ncbAccountStatus;
    }

    public BigDecimal getCreditAmountFirstNPL() {
        return creditAmountFirstNPL;
    }

    public void setCreditAmountFirstNPL(BigDecimal creditAmountFirstNPL) {
        this.creditAmountFirstNPL = creditAmountFirstNPL;
    }

    public boolean isNcbTDRFlag() {
        return ncbTDRFlag;
    }

    public void setNcbTDRFlag(boolean ncbTDRFlag) {
        this.ncbTDRFlag = ncbTDRFlag;
    }

    public SettlementStatus getCurrentPayment() {
        return currentPayment;
    }

    public void setCurrentPayment(SettlementStatus currentPayment) {
        this.currentPayment = currentPayment;
    }

    public SettlementStatus getPaymentPattern6M() {
        return paymentPattern6M;
    }

    public void setPaymentPattern6M(SettlementStatus paymentPattern6M) {
        this.paymentPattern6M = paymentPattern6M;
    }

    public SettlementStatus getPaymentPattern12M() {
        return paymentPattern12M;
    }

    public void setPaymentPattern12M(SettlementStatus paymentPattern12M) {
        this.paymentPattern12M = paymentPattern12M;
    }

    public int getOverDue31to60DaysWithinLast12Months() {
        return overDue31to60DaysWithinLast12Months;
    }

    public void setOverDue31to60DaysWithinLast12Months(int overDue31to60DaysWithinLast12Months) {
        this.overDue31to60DaysWithinLast12Months = overDue31to60DaysWithinLast12Months;
    }

    public int getOverLimitWithinLast6Months() {
        return overLimitWithinLast6Months;
    }

    public void setOverLimitWithinLast6Months(int overLimitWithinLast6Months) {
        this.overLimitWithinLast6Months = overLimitWithinLast6Months;
    }

    public AttributeTypeLevelAccountTypeNCB getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(AttributeTypeLevelAccountTypeNCB attributeType) {
        this.attributeType = attributeType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("ncbAccountStatus", ncbAccountStatus)
                .append("creditAmountFirstNPL", creditAmountFirstNPL)
                .append("ncbTDRFlag", ncbTDRFlag)
                .append("currentPayment", currentPayment)
                .append("paymentPattern6M", paymentPattern6M)
                .append("paymentPattern12M", paymentPattern12M)
                .append("overDue31to60DaysWithinLast12Months", overDue31to60DaysWithinLast12Months)
                .append("overLimitWithinLast6Months", overLimitWithinLast6Months)
                .append("attributeType", attributeType)
                .toString();
    }
}
