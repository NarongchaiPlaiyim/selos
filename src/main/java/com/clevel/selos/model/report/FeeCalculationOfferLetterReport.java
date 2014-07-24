package com.clevel.selos.model.report;

import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

public class FeeCalculationOfferLetterReport extends ReportModel {
    private long id;
    private String paymentMethod;
    private String paymentType;
    private BigDecimal amount;

    public FeeCalculationOfferLetterReport() {
        paymentMethod = getDefaultString();
        paymentMethod = getDefaultString();
        amount = getDefaultBigDecimal();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("paymentMethod", paymentMethod)
                .append("paymentType", paymentType)
                .append("amount", amount)
                .toString();
    }
}
