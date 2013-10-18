package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

public class BankStmtSrcCollateralProof {

    private Date OSBalanceEndDay;
    private BigDecimal OSBalanceAmount;

    public Date getOSBalanceEndDay() {
        return OSBalanceEndDay;
    }

    public void setOSBalanceEndDay(Date OSBalanceEndDay) {
        this.OSBalanceEndDay = OSBalanceEndDay;
    }

    public BigDecimal getOSBalanceAmount() {
        return OSBalanceAmount;
    }

    public void setOSBalanceAmount(BigDecimal OSBalanceAmount) {
        this.OSBalanceAmount = OSBalanceAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("OSBalanceEndDay", OSBalanceEndDay)
                .append("OSBalanceAmount", OSBalanceAmount)
                .toString();
    }
}
