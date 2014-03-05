package com.clevel.selos.model.view;

import java.io.Serializable;
import java.math.BigDecimal;

public class FeeCollectionDetailView implements Serializable  {
	private static final long serialVersionUID = -5072321499322605552L;
	private long id;
	private String paymentMethod;
	private String paymentType;
	private String description;
	private BigDecimal amount;
	private boolean agreementSign;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public boolean isAgreementSign() {
		return agreementSign;
	}
	public void setAgreementSign(boolean agreementSign) {
		this.agreementSign = agreementSign;
	}
}
