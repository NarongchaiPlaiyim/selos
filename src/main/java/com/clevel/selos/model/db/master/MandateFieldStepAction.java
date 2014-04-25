package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_man_fields_step_action")
public class MandateFieldStepAction implements Serializable{

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
    @JoinColumn(name = "mandate_fields_id")
    private MandateField mandateField;

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

    public MandateField getMandateField() {
        return mandateField;
    }

    public void setMandateField(MandateField mandateField) {
        this.mandateField = mandateField;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("action", action)
                .append("step", step)
                .toString();
    }
}