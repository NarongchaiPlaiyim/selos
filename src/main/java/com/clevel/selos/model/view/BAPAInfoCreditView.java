package com.clevel.selos.model.view;

import com.clevel.selos.model.BAPAType;
import com.clevel.selos.util.Util;

import java.io.Serializable;
import java.math.BigDecimal;

public class BAPAInfoCreditView implements Serializable,Comparable<BAPAInfoCreditView> {
	private static final long serialVersionUID = 891498608677139945L;

	private long id;
	
	private BAPAType type;
	
	//Read only field
	private long creditId;
	private String productProgram;
	private String creditType;
	private String loanPurpose;
	private boolean payByCustomer;
	
	private boolean fromCredit;
	private BigDecimal limit;
	private BigDecimal premiumAmount;
	private BigDecimal expenseAmount;
	
	private boolean needUpdate;
	
	public BAPAInfoCreditView() {
		
	}
	
	
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public BAPAType getType() {
		return type;
	}


	public void setType(BAPAType type) {
		this.type = type;
	}


	public long getCreditId() {
		return creditId;
	}


	public void setCreditId(long creditId) {
		this.creditId = creditId;
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


	public boolean isPayByCustomer() {
		return payByCustomer;
	}


	public void setPayByCustomer(boolean payByCustomer) {
		this.payByCustomer = payByCustomer;
	}


	public boolean isFromCredit() {
		return fromCredit;
	}


	public void setFromCredit(boolean fromCredit) {
		this.fromCredit = fromCredit;
	}


	public BigDecimal getLimit() {
		if (limit == null)
			return new BigDecimal(0);
		else
			return limit;
	}


	public void setLimit(BigDecimal limit) {
		this.limit = limit;
	}


	public BigDecimal getPremiumAmount() {
		if (premiumAmount == null)
			return new BigDecimal(0);
		else
			return premiumAmount;
	}


	public void setPremiumAmount(BigDecimal premiumAmount) {
		this.premiumAmount = premiumAmount;
	}

	public boolean isNeedUpdate() {
		return needUpdate;
	}


	public void setNeedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
	}


	public BigDecimal getExpenseAmount() {
		return expenseAmount;
	}
	public void setExpenseAmount(BigDecimal expenseAmount) {
		this.expenseAmount = expenseAmount;
	}
	
	@Override
	public int compareTo(BAPAInfoCreditView obj) {
		if (obj == null)
			return 1;
		if (this == obj)
			return 0;
		//compare from credit
		if (this.fromCredit != obj.fromCredit) {
			if (this.fromCredit)
				return -1;
			else
				return 1;
		}
		return Util.compareLong(id, obj.id);
	}
	
	public void updateValue(BAPAInfoCreditView view) {
		id = view.id;
		type = view.type;
		creditId = view.creditId;
		productProgram = view.productProgram;
		creditType = view.creditType;
		loanPurpose = view.loanPurpose;
		payByCustomer = view.payByCustomer;
		fromCredit = view.fromCredit;
		limit = view.limit;
		premiumAmount = view.premiumAmount;
		expenseAmount = view.expenseAmount;
		needUpdate = view.needUpdate;
	}
}
