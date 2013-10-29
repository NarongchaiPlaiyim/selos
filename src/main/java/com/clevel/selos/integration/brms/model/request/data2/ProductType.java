package com.clevel.selos.integration.brms.model.request.data2;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ProductType implements Serializable {
    private String productGroup; //todo : to be enum
    private BigDecimal totalProposedCreditLimit;

    private List<AttributeTypeLevelProductType> attributeType;
    private List<CollateralType> collateralTypes;
    private List<SELOSProductProgramType> selosProductProgramTypes;

    public ProductType() {
    }

    public ProductType(String productGroup, BigDecimal totalProposedCreditLimit, List<AttributeTypeLevelProductType> attributeType, List<CollateralType> collateralTypes, List<SELOSProductProgramType> selosProductProgramTypes) {
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

    public List<AttributeTypeLevelProductType> getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(List<AttributeTypeLevelProductType> attributeType) {
        this.attributeType = attributeType;
    }

    public List<CollateralType> getCollateralTypes() {
        return collateralTypes;
    }

    public void setCollateralTypes(List<CollateralType> collateralTypes) {
        this.collateralTypes = collateralTypes;
    }

    public List<SELOSProductProgramType> getSelosProductProgramTypes() {
        return selosProductProgramTypes;
    }

    public void setSelosProductProgramTypes(List<SELOSProductProgramType> selosProductProgramTypes) {
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
