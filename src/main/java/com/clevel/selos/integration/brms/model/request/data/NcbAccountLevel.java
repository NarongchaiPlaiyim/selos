package com.clevel.selos.integration.brms.model.request.data;

import com.clevel.selos.model.db.master.SettlementStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class NcbAccountLevel implements Serializable {
    private String ncbAccountStatus;
    private boolean tmbBankFlag;
    private boolean ncbNPLFlag;
    private BigDecimal creditAmountFirstNPL;
    private boolean ncbTDRFlag;
    private SettlementStatus currentPayment;
    private SettlementStatus paymentPattern6M;
    private SettlementStatus paymentPattern12M;
    private int overdue31dTo60dCount;
    private int overLimitLast6MthsCount;

    public NcbAccountLevel() {
    }

    public NcbAccountLevel(String ncbAccountStatus, boolean tmbBankFlag, boolean ncbNPLFlag, BigDecimal creditAmountFirstNPL, boolean ncbTDRFlag, SettlementStatus currentPayment, SettlementStatus paymentPattern6M, SettlementStatus paymentPattern12M, int overdue31dTo60dCount, int overLimitLast6MthsCount) {
        this.ncbAccountStatus = ncbAccountStatus;
        this.tmbBankFlag = tmbBankFlag;
        this.ncbNPLFlag = ncbNPLFlag;
        this.creditAmountFirstNPL = creditAmountFirstNPL;
        this.ncbTDRFlag = ncbTDRFlag;
        this.currentPayment = currentPayment;
        this.paymentPattern6M = paymentPattern6M;
        this.paymentPattern12M = paymentPattern12M;
        this.overdue31dTo60dCount = overdue31dTo60dCount;
        this.overLimitLast6MthsCount = overLimitLast6MthsCount;
    }

    public String getNcbAccountStatus() {
        return ncbAccountStatus;
    }

    public void setNcbAccountStatus(String ncbAccountStatus) {
        this.ncbAccountStatus = ncbAccountStatus;
    }

    public boolean isTmbBankFlag() {
        return tmbBankFlag;
    }

    public void setTmbBankFlag(boolean tmbBankFlag) {
        this.tmbBankFlag = tmbBankFlag;
    }

    public boolean isNcbNPLFlag() {
        return ncbNPLFlag;
    }

    public void setNcbNPLFlag(boolean ncbNPLFlag) {
        this.ncbNPLFlag = ncbNPLFlag;
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

    public int getOverdue31dTo60dCount() {
        return overdue31dTo60dCount;
    }

    public void setOverdue31dTo60dCount(int overdue31dTo60dCount) {
        this.overdue31dTo60dCount = overdue31dTo60dCount;
    }

    public int getOverLimitLast6MthsCount() {
        return overLimitLast6MthsCount;
    }

    public void setOverLimitLast6MthsCount(int overLimitLast6MthsCount) {
        this.overLimitLast6MthsCount = overLimitLast6MthsCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("ncbAccountStatus", ncbAccountStatus)
                .append("tmbBankFlag", tmbBankFlag)
                .append("ncbNPLFlag", ncbNPLFlag)
                .append("creditAmountFirstNPL", creditAmountFirstNPL)
                .append("ncbTDRFlag", ncbTDRFlag)
                .append("currentPayment", currentPayment)
                .append("paymentPattern6M", paymentPattern6M)
                .append("paymentPattern12M", paymentPattern12M)
                .append("overdue31dTo60dCount", overdue31dTo60dCount)
                .append("overLimitLast6MthsCount", overLimitLast6MthsCount)
                .toString();
    }
}
