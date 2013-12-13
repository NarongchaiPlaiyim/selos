package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

public class LoanPaymentDetailView implements Serializable {
    private Date signDate;
    private Date firstPayDate;
    private int payDate;

    public LoanPaymentDetailView(){}

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public Date getFirstPayDate() {
        return firstPayDate;
    }

    public void setFirstPayDate(Date firstPayDate) {
        this.firstPayDate = firstPayDate;
    }

    public int getPayDate() {
        return payDate;
    }

    public void setPayDate(int payDate) {
        this.payDate = payDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("signDate", signDate)
                .append("firstPayDate", firstPayDate)
                .append("payDate", payDate)
                .toString();
    }
}
