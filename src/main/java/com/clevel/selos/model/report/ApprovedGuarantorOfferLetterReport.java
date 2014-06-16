package com.clevel.selos.model.report;


import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

public class ApprovedGuarantorOfferLetterReport extends ReportModel{

    private String guarantorName;
    private BigDecimal totalLimitGuaranteeAmount;

    public ApprovedGuarantorOfferLetterReport() {
        guarantorName = getDefaultString();
        totalLimitGuaranteeAmount = getDefaultBigDecimal();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("guarantorName", guarantorName)
                .append("totalLimitGuaranteeAmount", totalLimitGuaranteeAmount)
                .toString();
    }

    public BigDecimal getTotalLimitGuaranteeAmount() {
        return totalLimitGuaranteeAmount;
    }

    public void setTotalLimitGuaranteeAmount(BigDecimal totalLimitGuaranteeAmount) {
        this.totalLimitGuaranteeAmount = totalLimitGuaranteeAmount;
    }

    public String getGuarantorName() {

        return guarantorName;
    }

    public void setGuarantorName(String guarantorName) {
        this.guarantorName = guarantorName;
    }
}
