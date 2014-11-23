package com.clevel.selos.model.db.audit;

import com.clevel.selos.model.ActionAudit;
import com.clevel.selos.model.ActionResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "adt_slos_activity")
public class SLOSActivity implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_ADT_SLOS_ID", sequenceName = "SEQ_ADT_SLOS_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ADT_SLOS_ID")
    private long id;

    @Column(name = "screen_id")
    private int screenId;

    @Column(name = "user_id", nullable = false, length = 10)
    private String userId;

    @Column(name = "app_number", length = 16)
    private String appNumber;

    @Column(name = "action_audit", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionAudit actionAudit;

    @Column(name = "action_desc", length = 1000)
    private String actionDesc;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "action_date", nullable = false)
    private Date actionDate;

    @Column(name = "action_result", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionResult actionResult;

    @Column(name = "result_desc", length = 3000)
    private String resultDesc;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "result_date", nullable = false)
    private Date resultDate;

    public SLOSActivity() {
    }

    public SLOSActivity(int screenId, String userId, String appNumber, ActionAudit actionAudit, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, Date resultDate) {
        this.screenId = screenId;
        this.userId = userId;
        this.actionAudit = actionAudit;
        this.actionDesc = actionDesc;
        this.actionDate = actionDate;
        this.actionResult = actionResult;
        this.resultDesc = resultDesc;
        this.resultDate = resultDate;
        this.appNumber = appNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ActionAudit getActionAudit() {
        return actionAudit;
    }

    public void setActionAudit(ActionAudit actionAudit) {
        this.actionAudit = actionAudit;
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

    public Date getResultDate() {
        return resultDate;
    }

    public void setResultDate(Date resultDate) {
        this.resultDate = resultDate;
    }

    public String getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(String appNumber) {
        this.appNumber = appNumber;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("screenId", screenId)
                .append("userId", userId)
                .append("appNumber", appNumber)
                .append("actionAudit", actionAudit)
                .append("actionDesc", actionDesc)
                .append("actionDate", actionDate)
                .append("actionResult", actionResult)
                .append("resultDesc", resultDesc)
                .append("resultDate", resultDate)
                .toString();
    }
}
