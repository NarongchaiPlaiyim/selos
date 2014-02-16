package com.clevel.selos.model.view;

import java.io.Serializable;
import java.math.BigDecimal;

import com.clevel.selos.model.BAPAType;

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
		if (type == null)
			return getPremiumAmount();
		switch (type) {
			case BA:
				if (limit != null && premiumAmount != null)
					return limit.subtract(premiumAmount);
				else if (limit != null) 
					return limit;
				else if (premiumAmount != null)
					return premiumAmount;
				else
					return new BigDecimal(0);
			default :
				return getPremiumAmount();
		}
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
				return 0;
		}
		return Long.compare(id, obj.id);
	}
}
