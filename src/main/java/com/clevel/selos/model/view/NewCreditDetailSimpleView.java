package com.clevel.selos.model.view;

import java.io.Serializable;
import java.math.BigDecimal;

public class NewCreditDetailSimpleView implements Serializable {
	private static final long serialVersionUID = 4706149760694897718L;
	private long id;
	private String accountName;
	private String accountNo;
	private String accountStatus;
	private String type;
	private String productProgram;
	private String creditFacility;
	private BigDecimal limit;
	private boolean hasAccountInfo;
	
	public NewCreditDetailSimpleView() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

	public BigDecimal getLimit() {
		return limit;
	}

	public void setLimit(BigDecimal limit) {
		this.limit = limit;
	}

	public boolean isHasAccountInfo() {
		return hasAccountInfo;
	}

	public void setHasAccountInfo(boolean hasAccountInfo) {
		this.hasAccountInfo = hasAccountInfo;
	}
	
}
