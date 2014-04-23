package com.clevel.selos.model.view;

import com.clevel.selos.model.ApprovalType;
import com.clevel.selos.model.DecisionType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

public class ApprovalHistoryView implements Serializable {
    private long id;
    private StepView stepView;
    private UserView userView;
    private RoleView roleView;
    private Date submitDate;
    private String comments;
    private DecisionType uwDecision;
    private int approvalType;
    private int isSubmit;

    public ApprovalHistoryView() {
        reset();
    }

    public void reset() {
        this.stepView = new StepView();
        this.userView = new UserView();
        this.roleView = new RoleView();
        this.submitDate = DateTime.now().toDate();
        this.comments = "";
        this.uwDecision = DecisionType.NO_DECISION;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public StepView getStepView() {
        return stepView;
    }

    public void setStepView(StepView stepView) {
        this.stepView = stepView;
    }

    public UserView getUserView() {
        return userView;
    }

    public void setUserView(UserView userView) {
        this.userView = userView;
    }

    public RoleView getRoleView() {
        return roleView;
    }

    public void setRoleView(RoleView roleView) {
        this.roleView = roleView;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public DecisionType getUwDecision() {
        return uwDecision;
    }

    public void setUwDecision(DecisionType uwDecision) {
        this.uwDecision = uwDecision;
    }

    public int getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(int approvalType) {
        this.approvalType = approvalType;
    }

    public int getSubmit() {
        return isSubmit;
    }

    public void setSubmit(int submit) {
        isSubmit = submit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("stepView", stepView)
                .append("userView", userView)
                .append("roleView", roleView)
                .append("submitDate", submitDate)
                .append("comments", comments)
                .append("uwDecision", uwDecision)
                .append("approvalType", approvalType)
                .append("isSubmit", isSubmit)
                .toString();
    }
}
