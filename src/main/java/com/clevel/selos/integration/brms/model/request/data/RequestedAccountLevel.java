package com.clevel.selos.integration.brms.model.request.data;

import com.clevel.selos.model.db.master.ProductProgram;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class RequestedAccountLevel implements Serializable {
    private ProductProgram productProgram;
    private String creditFacilityType; //todo: to be enum

    public RequestedAccountLevel() {
    }

    public RequestedAccountLevel(ProductProgram productProgram, String creditFacilityType) {
        this.productProgram = productProgram;
        this.creditFacilityType = creditFacilityType;
    }

    public ProductProgram getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(ProductProgram productProgram) {
        this.productProgram = productProgram;
    }

    public String getCreditFacilityType() {
        return creditFacilityType;
    }

    public void setCreditFacilityType(String creditFacilityType) {
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
