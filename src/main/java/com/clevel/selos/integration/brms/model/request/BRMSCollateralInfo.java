package com.clevel.selos.integration.brms.model.request;

import java.io.Serializable;
import java.math.BigDecimal;

public class BRMSCollateralInfo implements Serializable{
    private String collateralType;
    private String subCollateralType;
    private boolean appraisalFlag;
    private String aadDecision;
    private BigDecimal numberOfMonthsApprDate;
    private String collId;

    public String getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(String collateralType) {
        this.collateralType = collateralType;
    }

    public String getSubCollateralType() {
        return subCollateralType;
    }

    public void setSubCollateralType(String subCollateralType) {
        this.subCollateralType = subCollateralType;
    }

    public boolean isAppraisalFlag() {
        return appraisalFlag;
    }

    public void setAppraisalFlag(boolean appraisalFlag) {
        this.appraisalFlag = appraisalFlag;
    }

    public String getAadDecision() {
        return aadDecision;
    }

    public void setAadDecision(String aadDecision) {
        this.aadDecision = aadDecision;
    }

    public BigDecimal getNumberOfMonthsApprDate() {
        return numberOfMonthsApprDate;
    }

    public void setNumberOfMonthsApprDate(BigDecimal numberOfMonthsApprDate) {
        this.numberOfMonthsApprDate = numberOfMonthsApprDate;
    }

    public String getCollId() {
        return collId;
    }

    public void setCollId(String collId) {
        this.collId = collId;
    }
}
