package com.clevel.selos.integration.brms.model.request;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class BRMSBizInfo implements Serializable {
    private String id;
    private String bizCode;
    private boolean negativeFlag;
    private boolean highRiskFlag;
    private boolean esrFlag;
    private boolean suspendFlag;
    private boolean deviationFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
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

    public boolean isEsrFlag() {
        return esrFlag;
    }

    public void setEsrFlag(boolean esrFlag) {
        this.esrFlag = esrFlag;
    }

    public boolean isSuspendFlag() {
        return suspendFlag;
    }

    public void setSuspendFlag(boolean suspendFlag) {
        this.suspendFlag = suspendFlag;
    }

    public boolean isDeviationFlag() {
        return deviationFlag;
    }

    public void setDeviationFlag(boolean deviationFlag) {
        this.deviationFlag = deviationFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("bizCode", bizCode)
                .append("negativeFlag", negativeFlag)
                .append("highRiskFlag", highRiskFlag)
                .append("esrFlag", esrFlag)
                .append("suspendFlag", suspendFlag)
                .append("deviationFlag", deviationFlag)
                .toString();
    }
}
