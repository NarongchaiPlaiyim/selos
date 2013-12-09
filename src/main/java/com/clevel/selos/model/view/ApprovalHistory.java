package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

public class ApprovalHistory implements Serializable {
    private long id;
    private String action;
    private String comment;
    private int approveDecision;
    private User approver;
    private Date approveDate;

    public ApprovalHistory() {
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getApproveDecision() {
        return approveDecision;
    }

    public void setApproveDecision(int approveDecision) {
        this.approveDecision = approveDecision;
    }

    public User getApprover() {
        return approver;
    }

    public void setApprover(User approver) {
        this.approver = approver;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("action", action)
                .append("comment", comment)
                .append("approveDecision", approveDecision)
                .append("approver", approver)
                .append("approveDate", approveDate)
                .toString();
    }
}
