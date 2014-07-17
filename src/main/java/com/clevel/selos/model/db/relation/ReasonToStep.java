package com.clevel.selos.model.db.relation;

import com.clevel.selos.model.db.master.Action;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.Step;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rel_reason_step")
public class ReasonToStep implements Serializable {
    @Id
    @Column(name = "id")
    private long id;

    @OneToOne
    @JoinColumn(name = "reason_id")
    private Reason reason;

    @OneToOne
    @JoinColumn(name = "step_id")
    private Step step;

    @OneToOne
    @JoinColumn(name = "action_id")
    private Action action;

    @Column(name = "active")
    private int active;

    public ReasonToStep() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
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
                .append("reason", reason)
                .append("step", step)
                .append("action", action)
                .append("active", active)
                .toString();
    }
}
