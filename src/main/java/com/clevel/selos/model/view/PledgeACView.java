package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class PledgeACView implements Serializable {
    private String acAccountNumber;
    private String acNumberofDep;
    private BigDecimal acHoldAmount;

    public PledgeACView(){
        reset();
    }

    public void reset(){
        this.acAccountNumber = "";
        this.acHoldAmount = BigDecimal.ZERO;
        this.acNumberofDep = "";
    }

    public String getAcAccountNumber() {
        return acAccountNumber;
    }

    public void setAcAccountNumber(String acAccountNumber) {
        this.acAccountNumber = acAccountNumber;
    }

    public String getAcNumberofDep() {
        return acNumberofDep;
    }

    public void setAcNumberofDep(String acNumberofDep) {
        this.acNumberofDep = acNumberofDep;
    }

    public BigDecimal getAcHoldAmount() {
        return acHoldAmount;
    }

    public void setAcHoldAmount(BigDecimal acHoldAmount) {
        this.acHoldAmount = acHoldAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("acAccountNumber", acAccountNumber)
                .append("acNumberofDep", acNumberofDep)
                .append("acHoldAmount", acHoldAmount)
                .toString();
    }
}
