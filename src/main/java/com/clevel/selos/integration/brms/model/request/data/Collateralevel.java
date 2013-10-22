package com.clevel.selos.integration.brms.model.request.data;

import com.clevel.selos.model.db.master.CollateralType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class Collateralevel implements Serializable {
    private String collateralPotential;
    private CollateralType collateralType;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("collateralPotential", collateralPotential)
                .append("collateralType", collateralType)
                .toString();
    }
}
