package com.clevel.selos.model.view;


import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class LimitSetupView implements Serializable{

	private long id;
	private List<NewCreditDetailView> newCreditDetailViewList;

    private User modifyBy;
    private Date modifyDate;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("newCreditDetailView", getNewCreditDetailViewList())
                .toString();
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


	public List<NewCreditDetailView> getNewCreditDetailViewList() {
		return newCreditDetailViewList;
	}


	public void setNewCreditDetailViewList(List<NewCreditDetailView> newCreditDetailViewList) {
		this.newCreditDetailViewList = newCreditDetailViewList;
	}

}
