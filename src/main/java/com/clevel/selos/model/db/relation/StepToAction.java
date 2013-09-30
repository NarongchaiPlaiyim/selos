package com.clevel.selos.model.db.relation;

import com.clevel.selos.model.db.master.Action;
import com.clevel.selos.model.db.master.Status;
import com.clevel.selos.model.db.master.Step;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rel_step_action")
public class StepToAction implements Serializable {
    @Id
    @Column(name = "id")
    private long id;
    @OneToOne
    @JoinColumn(name = "step_id")
    private Step step;
    @OneToOne
    @JoinColumn(name = "action_id")
    private Action action;
    @OneToOne
    @JoinColumn(name = "current_status_id")
    private Status currentStatus;
    @OneToOne
    @JoinColumn(name = "next_status_id")
    private Status nextStatus;
    @Column(name = "active")
    private int active;

    public StepToAction() {
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

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Status getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Status currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Status getNextStatus() {
        return nextStatus;
    }

    public void setNextStatus(Status nextStatus) {
        this.nextStatus = nextStatus;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("step", step).
                append("action", action).
                append("currentStatus", currentStatus).
                append("nextStatus", nextStatus).
                append("active", active).
                toString();
    }
}
