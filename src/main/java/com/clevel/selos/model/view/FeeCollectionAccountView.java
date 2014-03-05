package com.clevel.selos.model.view;

import java.io.Serializable;
import java.math.BigDecimal;

import com.clevel.selos.model.FeeAccountType;

public class FeeCollectionAccountView implements Serializable  {

	private static final long serialVersionUID = -5178445826108945140L;
	private long id;
	private FeeAccountType feeAccountType;
	private String accountNo;
	private BigDecimal amount;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public FeeAccountType getFeeAccountType() {
		if (feeAccountType == null)
			feeAccountType = FeeAccountType.NA;
		return feeAccountType;
	}
	public void setFeeAccountType(FeeAccountType feeAccountType) {
		this.feeAccountType = feeAccountType;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
}
