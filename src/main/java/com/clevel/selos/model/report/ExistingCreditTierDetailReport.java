package com.clevel.selos.model.report;

import com.clevel.selos.model.view.BaseRateView;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

public class ExistingCreditTierDetailReport {

    private BigDecimal installment;
    private String finalBasePriceAndInterest;
    private int tenor;

    public ExistingCreditTierDetailReport() {
    }

    public BigDecimal getInstallment() {
        return installment;
    }

    public void setInstallment(BigDecimal installment) {
        this.installment = installment;
    }

    public String getFinalBasePriceAndInterest() {
        return finalBasePriceAndInterest;
    }

    public void setFinalBasePriceAndInterest(String finalBasePriceAndInterest) {
        this.finalBasePriceAndInterest = finalBasePriceAndInterest;
    }

    public int getTenor() {
        return tenor;
    }

    public void setTenor(int tenor) {
        this.tenor = tenor;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("installment", installment)
                .append("finalBasePriceAndInterest", finalBasePriceAndInterest)
                .append("tenor", tenor)
                .toString();
    }
}
