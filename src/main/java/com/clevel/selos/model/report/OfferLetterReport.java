package com.clevel.selos.model.report;


import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

public class OfferLetterReport extends ReportModel{

    private String borrowName;
    private Date finalApproved;
    private BigDecimal totalLimit;
    private BigDecimal valueMLR;
    private BigDecimal valueMOR;
    private BigDecimal valueMRR;
    private String bdmName;
    private String tel;
    private String zone;

    public OfferLetterReport() {
        borrowName = getDefaultString();
        finalApproved = getDefaultDate();
        totalLimit = getDefaultBigDecimal();
        valueMLR = getDefaultBigDecimal();
        valueMOR = getDefaultBigDecimal();
        valueMRR = getDefaultBigDecimal();
        bdmName = getDefaultString();
        tel = getDefaultString();
        zone = getDefaultString();
    }

    public String getBorrowName() {
        return borrowName;
    }

    public void setBorrowName(String borrowName) {
        this.borrowName = borrowName;
    }

    public Date getFinalApproved() {
        return finalApproved;
    }

    public void setFinalApproved(Date finalApproved) {
        this.finalApproved = finalApproved;
    }

    public BigDecimal getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(BigDecimal totalLimit) {
        this.totalLimit = totalLimit;
    }

    public BigDecimal getValueMLR() {
        return valueMLR;
    }

    public void setValueMLR(BigDecimal valueMLR) {
        this.valueMLR = valueMLR;
    }

    public BigDecimal getValueMOR() {
        return valueMOR;
    }

    public void setValueMOR(BigDecimal valueMOR) {
        this.valueMOR = valueMOR;
    }

    public BigDecimal getValueMRR() {
        return valueMRR;
    }

    public void setValueMRR(BigDecimal valueMRR) {
        this.valueMRR = valueMRR;
    }

    public String getBdmName() {
        return bdmName;
    }

    public void setBdmName(String bdmName) {
        this.bdmName = bdmName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("borrowName", borrowName)
                .append("finalApproved", finalApproved)
                .append("totalLimit", totalLimit)
                .append("valueMLR", valueMLR)
                .append("valueMOR", valueMOR)
                .append("valueMRR", valueMRR)
                .append("bdmName", bdmName)
                .append("tel", tel)
                .append("zone", zone)
                .toString();
    }
}
