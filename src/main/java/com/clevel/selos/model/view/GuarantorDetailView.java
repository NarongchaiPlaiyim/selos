package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

public class GuarantorDetailView {
    private Date guarantorSignDate;
    private String guarantorType;
    private BigDecimal guarantorAmount;
    private int approvedType;

    public GuarantorDetailView(){
        reset();

    }

    public void reset(){
        this.guarantorSignDate = new Date();
        this.guarantorType = "";
        this.guarantorAmount = BigDecimal.ZERO;
        this.approvedType = 0;
    }

    public Date getGuarantorSignDate() {
        return guarantorSignDate;
    }

    public void setGuarantorSignDate(Date guarantorSignDate) {
        this.guarantorSignDate = guarantorSignDate;
    }

    public String getGuarantorType() {
        return guarantorType;
    }

    public void setGuarantorType(String guarantorType) {
        this.guarantorType = guarantorType;
    }

    public BigDecimal getGuarantorAmount() {
        return guarantorAmount;
    }

    public void setGuarantorAmount(BigDecimal guarantorAmount) {
        this.guarantorAmount = guarantorAmount;
    }

    public int getApprovedType() {
        return approvedType;
    }

    public void setApprovedType(int approvedType) {
        this.approvedType = approvedType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("guarantorSignDate", guarantorSignDate)
                .append("guarantorType", guarantorType)
                .append("guarantorAmount", guarantorAmount)
                .append("approvedType", approvedType)
                .toString();
    }
}
