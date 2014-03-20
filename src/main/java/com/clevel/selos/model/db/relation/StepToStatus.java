package com.clevel.selos.model.db.relation;

import com.clevel.selos.model.db.master.Action;
import com.clevel.selos.model.db.master.Role;
import com.clevel.selos.model.db.master.Status;
import com.clevel.selos.model.db.master.Step;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rel_step_status_action_role")
public class StepToStatus implements Serializable {
    @Id
    @Column(name = "id")
    private long id;

    @OneToOne
    @JoinColumn(name = "step_id")
    private Step step;

    @OneToOne
    @JoinColumn(name = "current_status_id")
    private Status currentStatus;

    @OneToOne
    @JoinColumn(name = "action_id")
    private Action action;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_role_id")
    private Role fromRole;

    @OneToOne
    @JoinColumn(name = "active_role_id")
    private Role activeRole;

    @Column(name = "cadecisionflag", length = 10)
    private String caDecisionFlag;

    @Column(name = "pricingrequestflag", length = 10)
    private String pricingRequestFlag;

    @Column(name = "bu_uw_flag")
    private String buUwFlag;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_status_id")
    private Status nextStatus;

    @Column(name = "comments")
    private String comments;

    @Column(name = "active")
    private int active;


    public StepToStatus() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public Status getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Status currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Role getFromRole() {
        return fromRole;
    }

    public void setFromRole(Role fromRole) {
        this.fromRole = fromRole;
    }

    public Role getActiveRole() {
        return activeRole;
    }

    public void setActiveRole(Role activeRole) {
        this.activeRole = activeRole;
    }

    public String getCaDecisionFlag() {
        return caDecisionFlag;
    }

    public void setCaDecisionFlag(String caDecisionFlag) {
        this.caDecisionFlag = caDecisionFlag;
    }

    public String getPricingRequestFlag() {
        return pricingRequestFlag;
    }

    public void setPricingRequestFlag(String pricingRequestFlag) {
        this.pricingRequestFlag = pricingRequestFlag;
    }

    public String getBuUwFlag() {
        return buUwFlag;
    }

    public void setBuUwFlag(String buUwFlag) {
        this.buUwFlag = buUwFlag;
    }

    public Status getNextStatus() {
        return nextStatus;
    }

    public void setNextStatus(Status nextStatus) {
        this.nextStatus = nextStatus;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("step", step)
                .append("currentStatus", currentStatus)
                .append("action", action)
                .append("fromRole", fromRole)
                .append("activeRole", activeRole)
                .append("caDecisionFlag", caDecisionFlag)
                .append("pricingRequestFlag", pricingRequestFlag)
                .append("buUwFlag", buUwFlag)
                .append("nextStatus", nextStatus)
                .append("comments", comments)
                .append("active", active)
                .toString();
    }
}
