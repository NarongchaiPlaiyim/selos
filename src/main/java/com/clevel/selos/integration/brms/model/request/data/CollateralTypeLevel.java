package com.clevel.selos.integration.brms.model.request.data;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class CollateralTypeLevel implements Serializable {
    private String collateralType;

    private List<AppraisalTypeLevel> appraisalType;
    private AttributeTypeLevelCollateral attributeType;

    public CollateralTypeLevel() {
    }

    public CollateralTypeLevel(String collateralType, List<AppraisalTypeLevel> appraisalType, AttributeTypeLevelCollateral attributeType) {
        this.collateralType = collateralType;
        this.appraisalType = appraisalType;
        this.attributeType = attributeType;
    }

    public String getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(String collateralType) {
        this.collateralType = collateralType;
    }

    public List<AppraisalTypeLevel> getAppraisalType() {
        return appraisalType;
    }

    public void setAppraisalType(List<AppraisalTypeLevel> appraisalType) {
        this.appraisalType = appraisalType;
    }

    public AttributeTypeLevelCollateral getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(AttributeTypeLevelCollateral attributeType) {
        this.attributeType = attributeType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("collateralType", collateralType)
                .append("appraisalType", appraisalType)
                .append("attributeType", attributeType)
                .toString();
    }
}
