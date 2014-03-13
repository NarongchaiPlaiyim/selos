package com.clevel.selos.model.view;

import java.io.Serializable;
import java.math.BigDecimal;

public class BAPAInfoCreditToSelectView implements Serializable {
	private static final long serialVersionUID = -6732277946720126815L;
	private long id;
	private String productProgram;
	private String creditType;
	private String loanPurpose;
	private BigDecimal limit;
	private boolean isTopupBA;
	
	public BAPAInfoCreditToSelectView() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProductProgram() {
		return productProgram;
	}

	public void setProductProgram(String productProgram) {
		this.productProgram = productProgram;
	}

	public String getCreditType() {
		return creditType;
	}

	public void setCreditType(String creditType) {
		this.creditType = creditType;
	}

	public String getLoanPurpose() {
		return loanPurpose;
	}

	public void setLoanPurpose(String loanPurpose) {
		this.loanPurpose = loanPurpose;
	}

	public BigDecimal getLimit() {
		return limit;
	}

	public void setLimit(BigDecimal limit) {
		this.limit = limit;
	}

	public boolean isTopupBA() {
		return isTopupBA;
	}
	public void setTopupBA(boolean isTopupBA) {
		this.isTopupBA = isTopupBA;
	}
}
