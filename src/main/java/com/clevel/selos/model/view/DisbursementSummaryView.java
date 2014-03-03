package com.clevel.selos.model.view;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class DisbursementSummaryView implements Serializable{

	private long id;
	private long newCreditDetailID;
    private String productProgram ;
    private String creditFacility;
    private String code;
    private String productCode;
    private String projectCode;
    private String refinance;
    private BigDecimal approvedLimit;
    private BigDecimal holdAmount;
    private BigDecimal disburseAmount;
    private BigDecimal diffAmount;
    private boolean noFlag;

    public String getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(String productProgram) {
        this.productProgram = productProgram;
    }

    public String getCreditFacility() {
        return creditFacility;
    }

    public void setCreditFacility(String creditFacility) {
        this.creditFacility = creditFacility;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getRefinance() {
        return refinance;
    }

    public void setRefinance(String refinance) {
        this.refinance = refinance;
    }

    public BigDecimal getApprovedLimit() {
        return approvedLimit;
    }

    public void setApprovedLimit(BigDecimal approvedLimit) {
        this.approvedLimit = approvedLimit;
    }

    public BigDecimal getHoldAmount() {
        return holdAmount;
    }

    public void setHoldAmount(BigDecimal holdAmount) {
        this.holdAmount = holdAmount;
    }

    public BigDecimal getDisburseAmount() {
        return disburseAmount;
    }

    public void setDisburseAmount(BigDecimal disburseAmount) {
        this.disburseAmount = disburseAmount;
    }

    public BigDecimal getDiffAmount() {
        return diffAmount;
    }

    public void setDiffAmount(BigDecimal diffAmount) {
        this.diffAmount = diffAmount;
    }

    public boolean isNoFlag() {
        return noFlag;
    }

    public void setNoFlag(boolean noFlag) {
        this.noFlag = noFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("productProgram", productProgram)
                .append("creditFacility", creditFacility)
                .append("code", code)
                .append("productCode", productCode)
                .append("projectCode", projectCode)
                .append("refinance", refinance)
                .append("approvedLimit", approvedLimit)
                .append("holdAmount", holdAmount)
                .append("disburseAmount", disburseAmount)
                .append("diffAmount", diffAmount)
                .append("noFlag", noFlag)
                .toString();
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getNewCreditDetailID() {
		return newCreditDetailID;
	}

	public void setNewCreditDetailID(long newCreditDetailID) {
		this.newCreditDetailID = newCreditDetailID;
	}
}
