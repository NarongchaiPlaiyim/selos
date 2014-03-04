package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.Date;

public class LastUpdateDataView implements Serializable {
	private static final long serialVersionUID = -2976264168075487204L;
	private Date modifyDate;
	private String modifyUser;
	public LastUpdateDataView() {
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
}
