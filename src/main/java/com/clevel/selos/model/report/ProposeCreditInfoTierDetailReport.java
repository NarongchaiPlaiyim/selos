package com.clevel.selos.model.report;

import com.clevel.selos.model.view.BaseRateView;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

public class ProposeCreditInfoTierDetailReport {

    private String standardPriceLabel;
    private String suggestPriceLabel;
    private String finalPriceLabel;
    private BigDecimal installment;
    private int tenor;

    public ProposeCreditInfoTierDetailReport() {
    }

    public String getStandardPriceLabel() {
        return standardPriceLabel;
    }

    public void setStandardPriceLabel(String standardPriceLabel) {
        this.standardPriceLabel = standardPriceLabel;
    }

    public String getSuggestPriceLabel() {
        return suggestPriceLabel;
    }

    public void setSuggestPriceLabel(String suggestPriceLabel) {
        this.suggestPriceLabel = suggestPriceLabel;
    }

    public String getFinalPriceLabel() {
        return finalPriceLabel;
    }

    public void setFinalPriceLabel(String finalPriceLabel) {
        this.finalPriceLabel = finalPriceLabel;
    }

    public BigDecimal getInstallment() {
        return installment;
    }

    public void setInstallment(BigDecimal installment) {
        this.installment = installment;
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
                .append("standardPriceLabel", standardPriceLabel)
                .append("suggestPriceLabel", suggestPriceLabel)
                .append("finalPriceLabel", finalPriceLabel)
                .append("installment", installment)
                .append("tenor", tenor)
                .toString();
    }
}
