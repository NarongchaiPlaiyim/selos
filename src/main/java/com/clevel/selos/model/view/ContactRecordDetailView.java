package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.Status;
import com.clevel.selos.model.db.master.User;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    
    private Date nextCallingDateOnly;
    private int nextCallingHour = 0;
    private int nextCallingMin = 0;
    
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
		if (nextCallingDate != null) {
			Calendar calendar = Calendar.getInstance(Locale.US);
			calendar.setTime(nextCallingDate);
			nextCallingHour = calendar.get(Calendar.HOUR_OF_DAY);
			nextCallingMin = calendar.get(Calendar.MINUTE);
			nextCallingDateOnly = nextCallingDate;
		} else {
			nextCallingHour = 0;
			nextCallingMin = 0;
			nextCallingDateOnly = null;
		}
	}
	
	public void updateNextCallingDate() {
		if (nextCallingDateOnly == null) {
			nextCallingDate = null;
		} else {
			Calendar calendar = Calendar.getInstance(Locale.US);
			calendar.setTime(nextCallingDateOnly);
			calendar.set(Calendar.HOUR_OF_DAY,nextCallingHour);
			calendar.set(Calendar.MINUTE,nextCallingMin);
			nextCallingDate = calendar.getTime();
		}
	}
	public Date getNextCallingDateOnly() {
		return nextCallingDateOnly;
	}
	public void setNextCallingDateOnly(Date nextCallingDateOnly) {
		this.nextCallingDateOnly = nextCallingDateOnly;
	}
	public int getNextCallingHour() {
		return nextCallingHour;
	}
	public void setNextCallingHour(int nextCallingHour) {
		this.nextCallingHour = nextCallingHour;
	}
	public int getNextCallingMin() {
		return nextCallingMin;
	}
	public void setNextCallingMin(int nextCallingMin) {
		this.nextCallingMin = nextCallingMin;
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
