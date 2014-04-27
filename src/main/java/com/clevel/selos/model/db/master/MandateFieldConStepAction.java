package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_mandate_con_step_action")
public class MandateFieldConStepAction implements Serializable{

    @Id
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "action_id")
    private Action action;

    @ManyToOne
    @JoinColumn(name = "step_id")
    private Step step;

    @ManyToOne
    @JoinColumn(name = "mandate_con_id")
    private MandateFieldCondition mandateFieldCondition;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public MandateFieldCondition getMandateFieldCondition() {
        return mandateFieldCondition;
    }

    public void setMandateFieldCondition(MandateFieldCondition mandateFieldCondition) {
        this.mandateFieldCondition = mandateFieldCondition;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("action", action)
                .append("step", step)
                .append("mandateFieldCondition", mandateFieldCondition)
                .toString();
    }
}
