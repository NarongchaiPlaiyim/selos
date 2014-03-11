package com.clevel.selos.model.view;

import java.io.Serializable;
import java.math.BigDecimal;

public class FeeCollectionAccountView implements Serializable  {

	private static final long serialVersionUID = -5178445826108945140L;
	private long id;
	private String bankAccountType;
	private String accountNo;
	private BigDecimal amount;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getBankAccountType() {
		return bankAccountType;
	}
	public void setBankAccountType(String bankAccountType) {
		this.bankAccountType = bankAccountType;
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
