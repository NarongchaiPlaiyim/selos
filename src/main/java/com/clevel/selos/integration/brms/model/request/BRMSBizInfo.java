package com.clevel.selos.integration.brms.model.request;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class BRMSBizInfo implements Serializable {
    private String id;
    private String bizCode;
    private String negativeValue;
    private String highRiskValue;
    private String esrValue;
    private String suspendValue;
    private String deviationValue;

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

    public String getNegativeValue() {
        return negativeValue;
    }

    public void setNegativeValue(String negativeValue) {
        this.negativeValue = negativeValue;
    }

    public String getHighRiskValue() {
        return highRiskValue;
    }

    public void setHighRiskValue(String highRiskValue) {
        this.highRiskValue = highRiskValue;
    }

    public String getSuspendValue() {
        return suspendValue;
    }

    public void setSuspendValue(String suspendValue) {
        this.suspendValue = suspendValue;
    }

    public String getDeviationValue() {
        return deviationValue;
    }

    public void setDeviationValue(String deviationValue) {
        this.deviationValue = deviationValue;
    }

    public String getEsrValue() {
        return esrValue;
    }

    public void setEsrValue(String esrValue) {
        this.esrValue = esrValue;
    }
}
