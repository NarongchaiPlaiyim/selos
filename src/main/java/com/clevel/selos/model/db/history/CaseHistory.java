package com.clevel.selos.model.db.history;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "his_case_history")
public class CaseHistory implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_HIS_CASE_ID", sequenceName = "SEQ_HIS_CASE_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HIS_CASE_ID")
    private long id;
    @Column(name = "step_id")
    private Integer stepId;
    @Column(name = "step_name")
    private String stepName;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "action")
    private String action;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "ca_number")
    private String caNumber;
    @Column(name = "app_number")
    private String appNumber;
    @Column(name = "status_id")
    private Integer statusId;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "reason")
    private String reason;

    public CaseHistory() {
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getStepId() {
        return stepId;
    }

    public void setStepId(Integer stepId) {
        this.stepId = stepId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCaNumber() {
        return caNumber;
    }

    public void setCaNumber(String caNumber) {
        this.caNumber = caNumber;
    }

    public String getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(String appNumber) {
        this.appNumber = appNumber;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("stepId", stepId).
                append("stepName", stepName).
                append("userId", userId).
                append("action", action).
                append("createDate", createDate).
                append("caNumber", caNumber).
                append("appNumber", appNumber).
                append("statusId",statusId).
                append("reason",reason).
                append("remarks",remarks).
                toString();
    }
}
