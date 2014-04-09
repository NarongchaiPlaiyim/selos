package com.clevel.selos.model.view;

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
    private DecisionType approveDecision;
    private int isSubmit;
    private int approveType;

    public ApprovalHistoryView() {
        reset();
    }

    public void reset() {
        this.stepView = new StepView();
        this.userView = new UserView();
        this.roleView = new RoleView();
        this.submitDate = DateTime.now().toDate();
        this.comments = "";
        this.approveDecision = DecisionType.NO_DECISION;
        this.isSubmit = 0;
        this.approveType = 0;
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

    public DecisionType getApproveDecision() {
        return approveDecision;
    }

    public void setApproveDecision(DecisionType approveDecision) {
        this.approveDecision = approveDecision;
    }

    public int getSubmit() {
        return isSubmit;
    }

    public void setSubmit(int submit) {
        isSubmit = submit;
    }

    public int getApproveType() {
        return approveType;
    }

    public void setApproveType(int approveType) {
        this.approveType = approveType;
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
                .append("approveDecision", approveDecision)
                .append("isSubmit", isSubmit)
                .toString();
    }
}
