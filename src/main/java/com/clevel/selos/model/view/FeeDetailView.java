package com.clevel.selos.model.view;

import com.clevel.selos.model.FeeLevel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class FeeDetailView implements Serializable {

    private long id;
    private FeePaymentMethodView feePaymentMethodView;
    private FeeTypeView feeTypeView;
    private BigDecimal percentFee;
    private BigDecimal percentFeeAfter;
    private BigDecimal feeYear;
    private BigDecimal feeAmount;
    private FeeLevel feeLevel;
    private String description;
    private long creditDetailViewId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FeePaymentMethodView getFeePaymentMethodView() {
        return feePaymentMethodView;
    }

    public void setFeePaymentMethodView(FeePaymentMethodView feePaymentMethodView) {
        this.feePaymentMethodView = feePaymentMethodView;
    }

    public FeeTypeView getFeeTypeView() {
        return feeTypeView;
    }

    public void setFeeTypeView(FeeTypeView feeTypeView) {
        this.feeTypeView = feeTypeView;
    }

    public BigDecimal getPercentFee() {
        return percentFee;
    }

    public void setPercentFee(BigDecimal percentFee) {
        this.percentFee = percentFee;
    }

    public BigDecimal getPercentFeeAfter() {
        return percentFeeAfter;
    }

    public void setPercentFeeAfter(BigDecimal percentFeeAfter) {
        this.percentFeeAfter = percentFeeAfter;
    }

    public BigDecimal getFeeYear() {
        return feeYear;
    }

    public void setFeeYear(BigDecimal feeYear) {
        this.feeYear = feeYear;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public FeeLevel getFeeLevel() {
        return feeLevel;
    }

    public void setFeeLevel(FeeLevel feeLevel) {
        this.feeLevel = feeLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreditDetailViewId() {
        return creditDetailViewId;
    }

    public void setCreditDetailViewId(long creditDetailViewId) {
        this.creditDetailViewId = creditDetailViewId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("feePaymentMethodView", feePaymentMethodView)
                .append("feeTypeView", feeTypeView)
                .append("percentFee", percentFee)
                .append("percentFeeAfter", percentFeeAfter)
                .append("feeYear", feeYear)
                .append("feeAmount", feeAmount)
                .append("feeLevel", feeLevel)
                .append("description", description)
                .append("creditDetailViewId", creditDetailViewId)
                .toString();
    }
}
