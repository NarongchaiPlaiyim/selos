package com.clevel.selos.model.view.openaccount.model;
import java.io.Serializable;
import java.math.BigDecimal;

public class CreditTypeModel implements Serializable {
    private String productProgram;
    private String creditFacility;
    private BigDecimal Limit;

    public CreditTypeModel() {

    }

    public String getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(String productProgram) {
        this.productProgram = productProgram;
    }

    public String getCreditFacility() {
        return creditFacility;
    }

    public void setCreditFacility(String creditFacility) {
        this.creditFacility = creditFacility;
    }

    public BigDecimal getLimit() {
        return Limit;
    }

    public void setLimit(BigDecimal limit) {
        Limit = limit;
    }
}
