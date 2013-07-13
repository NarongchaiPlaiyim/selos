package com.clevel.selos.model.db;

import java.math.BigDecimal;

public class Facility {

    private BigDecimal id;
    private String facilityName;
    private BigDecimal requestAmount;

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
}
