package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

public class AccountInfoCreditTypeView {

    private long id;
    private boolean isSelected;
    private String productProgram;
    private BigDecimal limit;
    private String CreditFacility;


    public AccountInfoCreditTypeView(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(String productProgram) {
        this.productProgram = productProgram;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public String getCreditFacility() {
        return CreditFacility;
    }

    public void setCreditFacility(String creditFacility) {
        CreditFacility = creditFacility;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("productProgram", productProgram)
                .append("limit", limit)
                .append("CreditFacility", CreditFacility)
                .toString();
    }
}
