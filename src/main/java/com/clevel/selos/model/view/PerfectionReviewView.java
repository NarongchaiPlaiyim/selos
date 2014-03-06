package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.Date;

import com.clevel.selos.model.PerfectReviewStatus;
import com.clevel.selos.model.PerfectReviewType;

public class PerfectionReviewView implements Serializable {
	private static final long serialVersionUID = -50884450485089001L;
	private long id;
	private PerfectReviewType type;
	private Date date;
	private Date completedDate;
	private PerfectReviewStatus status;
	private String remark;
	private String modifyUser;
	private Date modifyDate;

	public PerfectionReviewView() {
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public PerfectReviewType getType() {
		if (type == null)
			type = PerfectReviewType.NA;
		return type;
	}

	public void setType(PerfectReviewType type) {
		this.type = type;
	}

	public PerfectReviewStatus getStatus() {
		if (status == null)
			status = PerfectReviewStatus.NA;
		return status;
	}

	public void setStatus(PerfectReviewStatus status) {
		this.status = status;
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
