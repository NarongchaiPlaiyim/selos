package com.clevel.selos.model.report;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

public class ProposeCreditInfoDetailReport {

    private int no;
    private String account;
    private String requsestType;
    private String productProgramViewName;
    private String creditTypeViewName;
    private BigDecimal limit;
    private String guaranteeAmount;
    private String productProgramAndCreditType;

    public ProposeCreditInfoDetailReport() {
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRequsestType() {
        return requsestType;
    }

    public void setRequsestType(String requsestType) {
        this.requsestType = requsestType;
    }

    public String getProductProgramViewName() {
        return productProgramViewName;
    }

    public void setProductProgramViewName(String productProgramViewName) {
        this.productProgramViewName = productProgramViewName;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public String getCreditTypeViewName() {
        return creditTypeViewName;
    }

    public void setCreditTypeViewName(String creditTypeViewName) {
        this.creditTypeViewName = creditTypeViewName;
    }

    public String getGuaranteeAmount() {
        return guaranteeAmount;
    }

    public void setGuaranteeAmount(String guaranteeAmount) {
        this.guaranteeAmount = guaranteeAmount;
    }

    public String getProductProgramAndCreditType() {
        return productProgramAndCreditType;
    }

    public void setProductProgramAndCreditType(String productProgramAndCreditType) {
        this.productProgramAndCreditType = productProgramAndCreditType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("no", no)
                .append("account", account)
                .append("requsestType", requsestType)
                .append("productProgramViewName", productProgramViewName)
                .append("creditTypeViewName", creditTypeViewName)
                .append("limit", limit)
                .append("guaranteeAmount", guaranteeAmount)
                .append("productProgramAndCreditType", productProgramAndCreditType)
                .toString();
    }
}
