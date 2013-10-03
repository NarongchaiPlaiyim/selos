package com.clevel.selos.model.db.audit;

import com.clevel.selos.model.ActionResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "adt_sec_activity")
public class SecurityActivity implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_ADT_SEC_ID", sequenceName="SEQ_ADT_SEC_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_ADT_SEC_ID")
    private Long id;
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
    @Enumerated(EnumType.STRING)
    private ActionResult actionResult;
    @Column(name="result_desc", length = 500)
    private String resultDesc;
    @Column(name="ip_address", length = 100)
    private String ipAddress;

    public SecurityActivity() {
    }

    public SecurityActivity(String userId, String action, String actionDesc,Date actionDate, ActionResult actionResult, String resultDesc, String ipAddress) {
        this.userId = userId;
        this.action = action;
        this.actionDesc = actionDesc;
        this.actionDate = actionDate;
        this.actionResult = actionResult;
        this.resultDesc = resultDesc;
        this.ipAddress = ipAddress;
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

    public ActionResult getActionResult() {
        return actionResult;
    }

    public void setActionResult(ActionResult actionResult) {
        this.actionResult = actionResult;
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
                append("actionResult", actionResult).
                append("resultDesc", resultDesc).
                append("ipAddress", ipAddress).
                toString();
    }
}
