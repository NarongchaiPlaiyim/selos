package com.clevel.selos.integration.brms.model.request.data;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class AttributeTypeLevelAppraisal implements Serializable{
    private String appraisalFlag; //todo : to be enum
    private String aadComment; //todo : to be enum
    private BigDecimal appraisalValueOfBuildingWithOwnershipDoc;
    private BigDecimal lengthOfAppraisal;

    public AttributeTypeLevelAppraisal() {
    }

    public AttributeTypeLevelAppraisal(String appraisalFlag, String aadComment, BigDecimal appraisalValueOfBuildingWithOwnershipDoc, BigDecimal lengthOfAppraisal) {
        this.appraisalFlag = appraisalFlag;
        this.aadComment = aadComment;
        this.appraisalValueOfBuildingWithOwnershipDoc = appraisalValueOfBuildingWithOwnershipDoc;
        this.lengthOfAppraisal = lengthOfAppraisal;
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

    public BigDecimal getAppraisalValueOfBuildingWithOwnershipDoc() {
        return appraisalValueOfBuildingWithOwnershipDoc;
    }

    public void setAppraisalValueOfBuildingWithOwnershipDoc(BigDecimal appraisalValueOfBuildingWithOwnershipDoc) {
        this.appraisalValueOfBuildingWithOwnershipDoc = appraisalValueOfBuildingWithOwnershipDoc;
    }

    public BigDecimal getLengthOfAppraisal() {
        return lengthOfAppraisal;
    }

    public void setLengthOfAppraisal(BigDecimal lengthOfAppraisal) {
        this.lengthOfAppraisal = lengthOfAppraisal;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("appraisalFlag", appraisalFlag)
                .append("aadComment", aadComment)
                .append("appraisalValueOfBuildingWithOwnershipDoc", appraisalValueOfBuildingWithOwnershipDoc)
                .append("lengthOfAppraisal", lengthOfAppraisal)
                .toString();
    }
}
