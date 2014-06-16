package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.Status;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

public class SubmitInfoView implements Serializable {
    private static final long serialVersionUID = -6869140956994592680L;

    private long id;
    private String appNumber;
    private Step step;
    private Status status;
    private String remark;
    private User fromUser;
    private User toUser;
    private Date submitDate;
    private int submitType;

    public SubmitInfoView(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(String appNumber) {
        this.appNumber = appNumber;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public int getSubmitType() {
        return submitType;
    }

    public void setSubmitType(int submitType) {
        this.submitType = submitType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("appNumber", appNumber)
                .append("step", step)
                .append("status", status)
                .append("remark", remark)
                .append("fromUser", fromUser)
                .append("toUser", toUser)
                .append("submitDate", submitDate)
                .append("submitType", submitType)
                .toString();
    }
}
