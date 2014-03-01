package com.clevel.selos.model.view;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class DisbursementCreditTypeView implements Serializable{

	private long id;
	private long newCreditDetailId;
    private String productProgram;
    private String creditFacility;
    private BigDecimal disburseAmount;
    private BigDecimal limitAmount;
    private String creditType;
    private String objective;

    private boolean componentFlag;
        

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("productProgram", getProductProgram())
                .append("disburseAmount", getDisburseAmount())
                .append("limitAmount", getLimitAmount())
                .toString();
    }

    


	public String getProductProgram() {
		return productProgram;
	}



	public void setProductProgram(String productProgram) {
		this.productProgram = productProgram;
	}



	public BigDecimal getDisburseAmount() {
		return disburseAmount;
	}



	public void setDisburseAmount(BigDecimal disburseAmount) {
		this.disburseAmount = disburseAmount;
	}



	public BigDecimal getLimitAmount() {
		return limitAmount;
	}



	public void setLimitAmount(BigDecimal limitAmount) {
		this.limitAmount = limitAmount;
	}



	public String getCreditFacility() {
		return creditFacility;
	}



	public void setCreditFacility(String creditFacility) {
		this.creditFacility = creditFacility;
	}




	public long getId() {
		return id;
	}




	public void setId(long id) {
		this.id = id;
	}




	public String getCreditType() {
		return creditType;
	}




	public void setCreditType(String creditType) {
		this.creditType = creditType;
	}




	public String getObjective() {
		return objective;
	}




	public void setObjective(String objective) {
		this.objective = objective;
	}

	public boolean isComponentFlag() {
		return componentFlag;
	}

	public void setComponentFlag(boolean componentFlag) {
		this.componentFlag = componentFlag;
	}




	public long getNewCreditDetailId() {
		return newCreditDetailId;
	}




	public void setNewCreditDetailId(long newCreditDetailId) {
		this.newCreditDetailId = newCreditDetailId;
	}

}
