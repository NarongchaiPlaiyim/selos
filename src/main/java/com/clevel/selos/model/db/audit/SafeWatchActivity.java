package com.clevel.selos.model.db.audit;

import com.clevel.selos.model.ActionResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "adt_sw_activity")
public class SafeWatchActivity implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_ADT_SW_ID", sequenceName="SEQ_ADT_SW_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_ADT_SW_ID")
    private long id;
    @Column(name="user_id", nullable=false)
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
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="result_date", nullable=false)
    private Date resultDate;
    @Column(name = "link_key")
    private String linkKey;

    public SafeWatchActivity() {
    }

    public SafeWatchActivity(String userId, String action, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, Date resultDate, String linkKey) {
        this.userId = userId;
        this.action = action;
        this.actionDesc = actionDesc;
        this.actionDate = actionDate;
        this.actionResult = actionResult;
        this.resultDesc = resultDesc;
        this.resultDate = resultDate;
        this.linkKey = linkKey;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Date getResultDate() {
        return resultDate;
    }

    public void setResultDate(Date resultDate) {
        this.resultDate = resultDate;
    }

    public String getLinkKey() {
        return linkKey;
    }

    public void setLinkKey(String linkKey) {
        this.linkKey = linkKey;
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
                append("resultDate", resultDate).
                append("linkKey", linkKey).
                toString();
    }
}
