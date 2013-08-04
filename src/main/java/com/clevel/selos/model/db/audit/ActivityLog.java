package com.clevel.selos.model.db.audit;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "adt_activity")
public class ActivityLog {
    @Id
    @SequenceGenerator(name="SEQ_ADT_ACTIVITY_ID", sequenceName="SEQ_ADT_ACTIVITY_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_ADT_ACTIVITY_ID")
    private Long id;
//    @Column(name="source_type", nullable=false)
//    private String sourceType;
//    @Column(name="source_module", nullable=false)
//    private String sourceModule;

    @Column(name="user_id")
    private String userId;
    @Column(name="action", nullable=false)
    private String action;
    @Column(name="action_desc", length = 500)
    private String actionDesc;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="action_date", nullable=false)
    private Date actionDate;
    @Column(name="result", nullable=false)
    private String result;
    @Column(name="result_desc", length = 500)
    private String resultDesc;
    @Column(name="ip_address", length = 100)
    private String ipAddress;

    public ActivityLog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getActionDesc() {
        return actionDesc;
    }

    public void setActionDesc(String actionDesc) {
        this.actionDesc = actionDesc;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("userId", userId).
                append("action", action).
                append("actionDesc", actionDesc).
                append("actionDate", actionDate).
                append("result", result).
                append("resultDesc", resultDesc).
                append("ipAddress", ipAddress).
                toString();
    }
}
