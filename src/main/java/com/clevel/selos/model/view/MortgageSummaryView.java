package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.Date;

import com.clevel.selos.model.MortgageSignLocationType;
import com.clevel.selos.model.db.master.User;

public class MortgageSummaryView implements Serializable {
    private static final long serialVersionUID = 5116739393766192381L;
	
    private long id;
    private long agreementId;
    private Date loanContractDate;
    private MortgageSignLocationType signingLocation;
    private String comsNumber;
    
    private int updLocation;
    
    private Date modifyDate;
    private User modifyBy;
    
    public MortgageSummaryView() {
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(long agreementId) {
		this.agreementId = agreementId;
	}

	public Date getLoanContractDate() {
		return loanContractDate;
	}

	public void setLoanContractDate(Date loanContractDate) {
		this.loanContractDate = loanContractDate;
	}

	public MortgageSignLocationType getSigningLocation() {
		if (signingLocation == null)
			return MortgageSignLocationType.NA;
		else
			return signingLocation;
	}

	public void setSigningLocation(MortgageSignLocationType signingLocation) {
		this.signingLocation = signingLocation;
	}

	public String getComsNumber() {
		return comsNumber;
	}

	public void setComsNumber(String comsNumber) {
		this.comsNumber = comsNumber;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public User getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(User modifyBy) {
		this.modifyBy = modifyBy;
	}
	
	public int getUpdLocation() {
		return updLocation;
	}
	
	public void setUpdLocation(int updLocation) {
		this.updLocation = updLocation;
	}
	
	public boolean requiredCalculate() {
		return id <= 0 || agreementId <=0;
	}
}
