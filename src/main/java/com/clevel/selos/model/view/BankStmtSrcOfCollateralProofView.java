package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BankStmtSrcOfCollateralProofView implements Serializable {

    private long id;
    private Date dateOfMaxBalance;
    private BigDecimal maxBalance;

    public BankStmtSrcOfCollateralProofView() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateOfMaxBalance() {
        return dateOfMaxBalance;
    }

    public void setDateOfMaxBalance(Date dateOfMaxBalance) {
        this.dateOfMaxBalance = dateOfMaxBalance;
    }

    public BigDecimal getMaxBalance() {
        return maxBalance;
    }

    public void setMaxBalance(BigDecimal maxBalance) {
        this.maxBalance = maxBalance;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("dateOfMaxBalance", dateOfMaxBalance)
                .append("maxBalance", maxBalance)
                .toString();
    }
}
