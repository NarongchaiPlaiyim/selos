package com.clevel.selos.integration.brms.model.request.data2;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class CollateralType implements Serializable {
    private String collateralType;

    private List<AppraisalType> appraisalType;
    private List<AttributeTypeLevelCollateral> attributeType;

    public CollateralType() {
    }

    public CollateralType(String collateralType, List<AppraisalType> appraisalType, List<AttributeTypeLevelCollateral> attributeType) {
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

    public List<AppraisalType> getAppraisalType() {
        return appraisalType;
    }

    public void setAppraisalType(List<AppraisalType> appraisalType) {
        this.appraisalType = appraisalType;
    }

    public List<AttributeTypeLevelCollateral> getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(List<AttributeTypeLevelCollateral> attributeType) {
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
