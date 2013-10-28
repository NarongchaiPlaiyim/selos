package com.clevel.selos.integration.brms.model.request.data;

import com.clevel.selos.model.db.master.CollateralType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class Collateralevel implements Serializable {
    private String collateralPotential;
    private CollateralType collateralType;
    private String appraisalFlag;
    private String aadComment;
    private BigDecimal lengthOfAppraisal;
    private String appraisalValueOfBuildin;

    public Collateralevel() {
    }

    public Collateralevel(String collateralPotential, CollateralType collateralType) {
        this.collateralPotential = collateralPotential;
        this.collateralType = collateralType;
    }

    public String getCollateralPotential() {
        return collateralPotential;
    }

    public void setCollateralPotential(String collateralPotential) {
        this.collateralPotential = collateralPotential;
    }

    public CollateralType getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(CollateralType collateralType) {
        this.collateralType = collateralType;
    }

    public String getAppraisalFlag() {
        return appraisalFlag;
    }

    public void setAppraisalFlag(String appraisalFlag) {
        this.appraisalFlag = appraisalFlag;
    }

    public String getAadComment() {
        return aadComment;
    }

    public void setAadComment(String aadComment) {
        this.aadComment = aadComment;
    }

    public BigDecimal getLengthOfAppraisal() {
        return lengthOfAppraisal;
    }

    public void setLengthOfAppraisal(BigDecimal lengthOfAppraisal) {
        this.lengthOfAppraisal = lengthOfAppraisal;
    }

    public String getAppraisalValueOfBuildin() {
        return appraisalValueOfBuildin;
    }

    public void setAppraisalValueOfBuildin(String appraisalValueOfBuildin) {
        this.appraisalValueOfBuildin = appraisalValueOfBuildin;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("collateralPotential", collateralPotential)
                .append("collateralType", collateralType)
                .toString();
    }
}
