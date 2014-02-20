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
    private StepView stepView;
    private UserView userView;
    private Date submitDate;
    private String comments;
    private DecisionType uwDecision;
    private int isSubmit;

    public ApprovalHistoryView() {
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
                .append("submitDate", submitDate)
                .append("comments", comments)
                .append("uwDecision", uwDecision)
                .append("isSubmit", isSubmit)
                .toString();
    }
}
