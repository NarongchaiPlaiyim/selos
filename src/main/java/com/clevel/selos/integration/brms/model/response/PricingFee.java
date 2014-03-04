package com.clevel.selos.integration.brms.model.response;

import com.clevel.selos.model.FeeLevel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class PricingFee implements Serializable{
    private FeeLevel feeLevel;
    private String creditDetailId;
    private String type;
    private String description;
    private BigDecimal feePercent;
    private BigDecimal feePercentAfterDiscount;
    private BigDecimal year;
    private BigDecimal amount;
    private String paymentMethod;

    public FeeLevel getFeeLevel() {
        return feeLevel;
    }

    public void setFeeLevel(FeeLevel feeLevel) {
        this.feeLevel = feeLevel;
    }

    public String getCreditDetailId() {
        return creditDetailId;
    }

    public void setCreditDetailId(String creditDetailId) {
        this.creditDetailId = creditDetailId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getFeePercent() {
        return feePercent;
    }

    public void setFeePercent(BigDecimal feePercent) {
        this.feePercent = feePercent;
    }

    public BigDecimal getFeePercentAfterDiscount() {
        return feePercentAfterDiscount;
    }

    public void setFeePercentAfterDiscount(BigDecimal feePercentAfterDiscount) {
        this.feePercentAfterDiscount = feePercentAfterDiscount;
    }

    public BigDecimal getYear() {
        return year;
    }

    public void setYear(BigDecimal year) {
        this.year = year;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("feeLevel", feeLevel)
                .append("creditDetailId", creditDetailId)
                .append("type", type)
                .append("description", description)
                .append("feePercent", feePercent)
                .append("feePercentAfterDiscount", feePercentAfterDiscount)
                .append("year", year)
                .append("amount", amount)
                .append("paymentMethod", paymentMethod)
                .toString();
    }
}
