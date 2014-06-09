package com.clevel.selos.model.view;


import com.clevel.selos.model.db.master.Action;
import com.clevel.selos.model.db.master.Step;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class MandateFieldClassStepActionView implements Serializable{

    private long id;
    private ActionView actionView;
    private StepView stepView;
    private MandateFieldClassView mandateFieldClassView;
    private boolean needUpdate = false;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ActionView getActionView() {
        return actionView;
    }

    public void setActionView(ActionView actionView) {
        this.actionView = actionView;
    }

    public StepView getStepView() {
        return stepView;
    }

    public void setStepView(StepView stepView) {
        this.stepView = stepView;
    }

    public MandateFieldClassView getMandateFieldClassView() {
        return mandateFieldClassView;
    }

    public void setMandateFieldClassView(MandateFieldClassView mandateFieldClassView) {
        this.mandateFieldClassView = mandateFieldClassView;
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("actionView", actionView)
                .append("stepView", stepView)
                .append("mandateFieldClassView", mandateFieldClassView)
                .append("needUpdate", needUpdate)
                .toString();
    }
}
