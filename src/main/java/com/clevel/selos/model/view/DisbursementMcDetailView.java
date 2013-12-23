package com.clevel.selos.model.view;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class DisbursementMcDetailView implements Serializable{

    private String issuedBy;
    private Date issuedDate;
    private String payeeName;
    private String payeeSubname;
    private String crossType;
    private List<CreditTypeDetailView> creditType;
    private BigDecimal totalAmount;

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeSubname() {
        return payeeSubname;
    }

    public void setPayeeSubname(String payeeSubname) {
        this.payeeSubname = payeeSubname;
    }

    public String getCrossType() {
        return crossType;
    }

    public void setCrossType(String crossType) {
        this.crossType = crossType;
    }

    public List<CreditTypeDetailView> getCreditType() {
        return creditType;
    }

    public void setCreditType(List<CreditTypeDetailView> creditType) {
        this.creditType = creditType;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("issuedBy", issuedBy)
                .append("issuedDate", issuedDate)
                .append("payeeName", payeeName)
                .append("payeeSubname",payeeSubname)
                .append("crossType", crossType)
                .append("creditType", creditType)
                .append("totalAmount",totalAmount)
                .toString();
    }

}
