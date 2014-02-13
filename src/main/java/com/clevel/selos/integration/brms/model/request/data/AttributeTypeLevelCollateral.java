package com.clevel.selos.integration.brms.model.request.data;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class AttributeTypeLevelCollateral implements Serializable {
    private String collateralPotential; //todo : to be enum

    public AttributeTypeLevelCollateral() {
    }

    public AttributeTypeLevelCollateral(String collateralPotential) {
        this.collateralPotential = collateralPotential;
    }

    public String getCollateralPotential() {
        return collateralPotential;
    }

    public void setCollateralPotential(String collateralPotential) {
        this.collateralPotential = collateralPotential;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("collateralPotential", collateralPotential)
                .toString();
    }
}
