package com.clevel.selos.model.report;

import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

public class DisbursementOfferLetterReport extends ReportModel {

    private String productProgram;
    private BigDecimal total;
    private String limit;
    private String name;

    public DisbursementOfferLetterReport() {
        productProgram = getDefaultString();
        total = getDefaultBigDecimal();
        limit = getDefaultString();
        name = getDefaultString();
    }

    public String getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(String productProgram) {
        this.productProgram = productProgram;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("productProgram", productProgram)
                .append("total", total)
                .append("limit", limit)
                .append("name", name)
                .toString();
    }
}
