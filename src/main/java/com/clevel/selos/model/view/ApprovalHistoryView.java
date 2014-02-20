package com.clevel.selos.model.view;

import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.db.master.Action;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

public class ApprovalHistoryView implements Serializable {
    private long id;
    private String action;
    private UserView actionBy;
    private String comment;
    private Date approveDate;
    private DecisionType uwDecision;

    public ApprovalHistoryView() {
        reset();
    }

    public void reset() {
        this.action = "";
        this.approveDate = DateTime.now().toDate();
        this.comment = "";
        this.uwDecision = DecisionType.NO_DECISION;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public UserView getActionBy() {
        return actionBy;
    }

    public void setActionBy(UserView actionBy) {
        this.actionBy = actionBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public DecisionType getUwDecision() {
        return uwDecision;
    }

    public void setUwDecision(DecisionType uwDecision) {
        this.uwDecision = uwDecision;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("action", action)
                .append("actionBy", actionBy)
                .append("comment", comment)
                .append("approveDate", approveDate)
                .append("uwDecision", uwDecision)
                .toString();
    }
}
