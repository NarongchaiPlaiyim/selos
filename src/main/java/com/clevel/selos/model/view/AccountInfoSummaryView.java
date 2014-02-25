package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.Date;

import com.clevel.selos.model.db.master.User;

public class AccountInfoSummaryView implements Serializable {
	private static final long serialVersionUID = -8089859901828967895L;
	private long id;
    private User modifyBy;
    private Date modifyDate;
    
    public AccountInfoSummaryView() {
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(User modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
    
}
