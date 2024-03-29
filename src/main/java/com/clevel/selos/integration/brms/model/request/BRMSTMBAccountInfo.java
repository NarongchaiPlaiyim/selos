package com.clevel.selos.integration.brms.model.request;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class BRMSTMBAccountInfo implements Serializable{

    private boolean activeFlag;
    private String dataSource;
    private String accountRef;
    private String custToAccountRelationCD;
    private String tmbTDRFlag;
    private BigDecimal numMonthIntPastDue;
    private BigDecimal numMonthIntPastDueTDRAcc;
    private BigDecimal tmbDelPriDay;
    private BigDecimal tmbDelIntDay;
    private String tmbBlockCode;

    public boolean isActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getAccountRef() {
        return accountRef;
    }

    public void setAccountRef(String accountRef) {
        this.accountRef = accountRef;
    }

    public String getCustToAccountRelationCD() {
        return custToAccountRelationCD;
    }

    public void setCustToAccountRelationCD(String custToAccountRelationCD) {
        this.custToAccountRelationCD = custToAccountRelationCD;
    }

    public String getTmbTDRFlag() {
        return tmbTDRFlag;
    }

    public void setTmbTDRFlag(String tmbTDRFlag) {
        this.tmbTDRFlag = tmbTDRFlag;
    }

    public BigDecimal getNumMonthIntPastDue() {
        return numMonthIntPastDue;
    }

    public void setNumMonthIntPastDue(BigDecimal numMonthIntPastDue) {
        this.numMonthIntPastDue = numMonthIntPastDue;
    }

    public BigDecimal getNumMonthIntPastDueTDRAcc() {
        return numMonthIntPastDueTDRAcc;
    }

    public void setNumMonthIntPastDueTDRAcc(BigDecimal numMonthIntPastDueTDRAcc) {
        this.numMonthIntPastDueTDRAcc = numMonthIntPastDueTDRAcc;
    }

    public BigDecimal getTmbDelPriDay() {
        return tmbDelPriDay;
    }

    public void setTmbDelPriDay(BigDecimal tmbDelPriDay) {
        this.tmbDelPriDay = tmbDelPriDay;
    }

    public BigDecimal getTmbDelIntDay() {
        return tmbDelIntDay;
    }

    public void setTmbDelIntDay(BigDecimal tmbDelIntDay) {
        this.tmbDelIntDay = tmbDelIntDay;
    }

    public String getTmbBlockCode() {
        return tmbBlockCode;
    }

    public void setTmbBlockCode(String tmbBlockCode) {
        this.tmbBlockCode = tmbBlockCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("activeFlag", activeFlag)
                .append("dataSource", dataSource)
                .append("accountRef", accountRef)
                .append("custToAccountRelationCD", custToAccountRelationCD)
                .append("tmbTDRFlag", tmbTDRFlag)
                .append("numMonthIntPastDue", numMonthIntPastDue)
                .append("numMonthIntPastDueTDRAcc", numMonthIntPastDueTDRAcc)
                .append("tmbDelPriDay", tmbDelPriDay)
                .append("tmbDelIntDay", tmbDelIntDay)
                .append("tmbBlockCode", tmbBlockCode)
                .toString();
    }
}
