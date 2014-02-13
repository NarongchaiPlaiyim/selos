package com.clevel.selos.integration.brms.model.request.data;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class SELOSProductProgramTypeLevel implements Serializable {
    private String productProgram; //todo : to be enum
    private List<CreditFacilityTypeLevel> creditFacilityType;

    public SELOSProductProgramTypeLevel() {
    }

    public SELOSProductProgramTypeLevel(String productProgram, List<CreditFacilityTypeLevel> creditFacilityType) {
        this.productProgram = productProgram;
        this.creditFacilityType = creditFacilityType;
    }

    public String getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(String productProgram) {
        this.productProgram = productProgram;
    }

    public List<CreditFacilityTypeLevel> getCreditFacilityType() {
        return creditFacilityType;
    }

    public void setCreditFacilityType(List<CreditFacilityTypeLevel> creditFacilityType) {
        this.creditFacilityType = creditFacilityType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("productProgram", productProgram)
                .append("creditFacilityType", creditFacilityType)
                .toString();
    }
}