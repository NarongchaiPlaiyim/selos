package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_mandate_class_step_action")
public class MandateFieldClassStepAction implements Serializable{

    @Id
    @SequenceGenerator(name = "SEQ_MST_MAN_FIELD_CLASS_STEP", sequenceName = "SEQ_MST_MAN_FIELD_CLASS_STEP", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MST_MAN_FIELD_CLASS_STEP")
    private long id;

    @ManyToOne
    @JoinColumn(name = "action_id")
    private Action action;

    @ManyToOne
    @JoinColumn(name = "step_id")
    private Step step;

    @ManyToOne
    @JoinColumn(name = "mandate_class_id")
    private MandateFieldClass mandateFieldClass;

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

    public MandateFieldClass getMandateFieldClass() {
        return mandateFieldClass;
    }

    public void setMandateFieldClass(MandateFieldClass mandateFieldClass) {
        this.mandateFieldClass = mandateFieldClass;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("action", action)
                .append("step", step)
                .append("mandateFieldClass", mandateFieldClass)
                .toString();
    }
}
