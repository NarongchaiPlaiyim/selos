package com.clevel.selos.integration.brms.model.request.data;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ProductTypeLevel implements Serializable {
    private String productGroup; //todo : to be enum
    private BigDecimal totalProposedCreditLimit;

    private AttributeTypeLevelProductType attributeType;
    private List<CollateralTypeLevel> collateralTypes;
    private List<SELOSProductProgramTypeLevel> selosProductProgramTypes;

    public ProductTypeLevel() {
    }

    public ProductTypeLevel(String productGroup, BigDecimal totalProposedCreditLimit, AttributeTypeLevelProductType attributeType, List<CollateralTypeLevel> collateralTypes, List<SELOSProductProgramTypeLevel> selosProductProgramTypes) {
        this.productGroup = productGroup;
        this.totalProposedCreditLimit = totalProposedCreditLimit;
        this.attributeType = attributeType;
        this.collateralTypes = collateralTypes;
        this.selosProductProgramTypes = selosProductProgramTypes;
    }

    public String getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

    public BigDecimal getTotalProposedCreditLimit() {
        return totalProposedCreditLimit;
    }

    public void setTotalProposedCreditLimit(BigDecimal totalProposedCreditLimit) {
        this.totalProposedCreditLimit = totalProposedCreditLimit;
    }

    public AttributeTypeLevelProductType getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(AttributeTypeLevelProductType attributeType) {
        this.attributeType = attributeType;
    }

    public List<CollateralTypeLevel> getCollateralTypes() {
        return collateralTypes;
    }

    public void setCollateralTypes(List<CollateralTypeLevel> collateralTypes) {
        this.collateralTypes = collateralTypes;
    }

    public List<SELOSProductProgramTypeLevel> getSelosProductProgramTypes() {
        return selosProductProgramTypes;
    }

    public void setSelosProductProgramTypes(List<SELOSProductProgramTypeLevel> selosProductProgramTypes) {
        this.selosProductProgramTypes = selosProductProgramTypes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("productGroup", productGroup)
                .append("totalProposedCreditLimit", totalProposedCreditLimit)
                .append("attributeType", attributeType)
                .append("collateralTypes", collateralTypes)
                .append("selosProductProgramTypes", selosProductProgramTypes)
                .toString();
    }
}
