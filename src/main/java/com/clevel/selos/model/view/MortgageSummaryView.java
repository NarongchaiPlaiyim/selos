package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.Date;

import com.clevel.selos.model.MortgageConfirmedType;
import com.clevel.selos.model.db.master.User;

public class MortgageSummaryView implements Serializable {
    private static final long serialVersionUID = 5116739393766192381L;
	
    private long id;
    private MortgageConfirmedType confirmed;
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
	public MortgageConfirmedType getConfirmed() {
		if (confirmed == null)
			confirmed = MortgageConfirmedType.NA;
		return confirmed;
	}
	public void setConfirmed(MortgageConfirmedType confirmed) {
		this.confirmed = confirmed;
	}
}
