package com.clevel.selos.integration.brms.model.request.data2;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class SELOSProductProgramType implements Serializable {
    private String productProgram; //todo : to be enum
    private List<CreditFacilityType> creditFacilityType;

    public SELOSProductProgramType() {
    }

    public SELOSProductProgramType(String productProgram, List<CreditFacilityType> creditFacilityType) {
        this.productProgram = productProgram;
        this.creditFacilityType = creditFacilityType;
    }

    public String getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(String productProgram) {
        this.productProgram = productProgram;
    }

    public List<CreditFacilityType> getCreditFacilityType() {
        return creditFacilityType;
    }

    public void setCreditFacilityType(List<CreditFacilityType> creditFacilityType) {
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
