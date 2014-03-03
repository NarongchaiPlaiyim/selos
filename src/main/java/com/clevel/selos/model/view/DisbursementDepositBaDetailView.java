package com.clevel.selos.model.view;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DisbursementDepositBaDetailView implements Serializable{

	private long id;
	private long openAccountId;
    private String accountNumber;
    private String accountName;
    private List<DisbursementCreditTypeView> disbursementCreditTypeView;
    private BigDecimal totalAmount;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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
                .append("accountNumber", accountNumber)
                .append("accountName", accountName)
                .append("creditType", disbursementCreditTypeView)
                .append("totalAmount", totalAmount)
                .toString();
    }

	public List<DisbursementCreditTypeView> getDisbursementCreditTypeView() {
		return disbursementCreditTypeView;
	}

	public void setDisbursementCreditTypeView(List<DisbursementCreditTypeView> disbursementCreditTypeView) {
		this.disbursementCreditTypeView = disbursementCreditTypeView;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOpenAccountId() {
		return openAccountId;
	}

	public void setOpenAccountId(long openAccountId) {
		this.openAccountId = openAccountId;
	}
}
