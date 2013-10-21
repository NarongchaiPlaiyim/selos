package com.clevel.selos.integration.brms.model.request.data;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class BusinessLevel implements Serializable{
    private int businessSeqId;
    private String businesscode;
    private boolean negativeFlag;
    private boolean highRiskFlag;

    public BusinessLevel() {
    }

    public BusinessLevel(int businessSeqId, String businesscode, boolean negativeFlag, boolean highRiskFlag) {
        this.businessSeqId = businessSeqId;
        this.businesscode = businesscode;
        this.negativeFlag = negativeFlag;
        this.highRiskFlag = highRiskFlag;
    }

    public int getBusinessSeqId() {
        return businessSeqId;
    }

    public void setBusinessSeqId(int businessSeqId) {
        this.businessSeqId = businessSeqId;
    }

    public String getBusinesscode() {
        return businesscode;
    }

    public void setBusinesscode(String businesscode) {
        this.businesscode = businesscode;
    }

    public boolean isNegativeFlag() {
        return negativeFlag;
    }

    public void setNegativeFlag(boolean negativeFlag) {
        this.negativeFlag = negativeFlag;
    }

    public boolean isHighRiskFlag() {
        return highRiskFlag;
    }

    public void setHighRiskFlag(boolean highRiskFlag) {
        this.highRiskFlag = highRiskFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("businessSeqId", businessSeqId)
                .append("businesscode", businesscode)
                .append("negativeFlag", negativeFlag)
                .append("highRiskFlag", highRiskFlag)
                .toString();
    }
}
