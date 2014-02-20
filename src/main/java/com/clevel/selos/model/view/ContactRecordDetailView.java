package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.Status;
import com.clevel.selos.model.db.master.User;

public class ContactRecordDetailView implements Serializable {
	private static final long serialVersionUID = 466737071113972055L;
	private long id;
    private Date callingDate;
    private int callingResult;
    private int acceptResult;
    private Date nextCallingDate;
    private Reason reason;
    private String remark;
    private Status status;
    private User createBy;
    private boolean needUpdate;
    
    private int updReasonId;
    
    public ContactRecordDetailView() {
    }
    
    
    public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public Date getCallingDate() {
		return callingDate;
	}


	public void setCallingDate(Date callingDate) {
		this.callingDate = callingDate;
	}


	public int getCallingResult() {
		return callingResult;
	}


	public void setCallingResult(int callingResult) {
		this.callingResult = callingResult;
	}


	public int getAcceptResult() {
		return acceptResult;
	}


	public void setAcceptResult(int acceptResult) {
		this.acceptResult = acceptResult;
	}


	public Date getNextCallingDate() {
		return nextCallingDate;
	}


	public void setNextCallingDate(Date nextCallingDate) {
		this.nextCallingDate = nextCallingDate;
	}


	public Reason getReason() {
		return reason;
	}


	public void setReason(Reason reason) {
		this.reason = reason;
		if (reason != null)
			this.updReasonId = reason.getId();
		else
			this.updReasonId = 0;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public User getCreateBy() {
		return createBy;
	}


	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}
	
	public boolean isNeedUpdate() {
		return needUpdate;
	}
	
	public void setNeedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
	}
	
	public boolean isNew() {
		return id <= 0;
	}
	
	public int getUpdReasonId() {
		return updReasonId;
	}
	public void setUpdReasonId(int updReasonId) {
		this.updReasonId = updReasonId;
	}
	
	@Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("callingDate", callingDate)
                .append("callingResult", callingResult)
                .append("acceptResult", acceptResult)
                .append("nextCallingDate", nextCallingDate)
                .append("reason", reason)
                .append("remark", remark)
                .append("status", status)
                .append("createBy", createBy)
                .append("updReasonId",updReasonId)
                .toString();
    }
}
