package com.clevel.selos.model.report;

import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

public class PriceFeeDecisionReport extends ReportModel{

    private BigDecimal intFeeDOA;
    private BigDecimal frontendFeeDOA;
    private BigDecimal guarantorBA;
    private String reasonForReduction;

    public PriceFeeDecisionReport() {
    }

    public BigDecimal getFrontendFeeDOA() {
        return frontendFeeDOA;
    }

    public void setFrontendFeeDOA(BigDecimal frontendFeeDOA) {
        this.frontendFeeDOA = frontendFeeDOA;
    }

    public BigDecimal getGuarantorBA() {
        return guarantorBA;
    }

    public void setGuarantorBA(BigDecimal guarantorBA) {
        this.guarantorBA = guarantorBA;
    }

    public BigDecimal getIntFeeDOA() {
        return intFeeDOA;
    }

    public void setIntFeeDOA(BigDecimal intFeeDOA) {
        this.intFeeDOA = intFeeDOA;
    }

    public String getReasonForReduction() {
        return reasonForReduction;
    }

    public void setReasonForReduction(String reasonForReduction) {
        this.reasonForReduction = reasonForReduction;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("frontendFeeDOA", frontendFeeDOA)
                .append("intFeeDOA", intFeeDOA)
                .append("guarantorBA", guarantorBA)
                .append("reasonForReduction", reasonForReduction)
                .toString();
    }
}
