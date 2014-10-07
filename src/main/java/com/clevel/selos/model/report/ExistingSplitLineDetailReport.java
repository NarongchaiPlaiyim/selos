package com.clevel.selos.model.report;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

public class ExistingSplitLineDetailReport {

    private String productProgramName;
    private BigDecimal limit;

    public ExistingSplitLineDetailReport() {
    }

    public String getProductProgramName() {
        return productProgramName;
    }

    public void setProductProgramName(String productProgramName) {
        this.productProgramName = productProgramName;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("productProgramName", productProgramName)
                .append("limit", limit)
                .toString();
    }
}
