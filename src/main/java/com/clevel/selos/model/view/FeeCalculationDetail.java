package com.clevel.selos.model.view;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class FeeCalculationDetail implements Serializable {
	private static final long serialVersionUID = -1810924236273477345L;
	private String feeType;
	private String formula;
	private BigDecimal amount;
	
	public FeeCalculationDetail() {
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("feeType", feeType)
                .append("formula", formula)
                .append("amount", amount)
                .toString();
    }
}
