package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.Date;

import com.clevel.selos.model.RadioValue;

public class FeeSummaryView implements Serializable  {
	private static final long serialVersionUID = 2241253912774072062L;
	private long id;
	private RadioValue collectCompleted;
	private String modifyUser;
	private Date modifyDate;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public RadioValue getCollectCompleted() {
		if (collectCompleted == null)
			collectCompleted = RadioValue.NA;
		return collectCompleted;
	}
	public void setCollectCompleted(RadioValue collectCompleted) {
		this.collectCompleted = collectCompleted;
	}
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
}
