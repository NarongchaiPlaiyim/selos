package com.clevel.selos.model.view;

import java.math.BigDecimal;

public class Facility {

    private BigDecimal id;
    private String facilityName;
    private BigDecimal requestAmount;
    private String productProgramName;

    public Facility(){
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public BigDecimal getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(BigDecimal requestAmount) {
        this.requestAmount = requestAmount;
    }


    public String getProductProgramName() {
        return productProgramName;
    }

    public void setProductProgramName(String productProgramName) {
        this.productProgramName = productProgramName;
    }
}
