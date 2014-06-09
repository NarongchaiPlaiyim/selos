package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class MandateFieldStepActionView implements Serializable{

    private StepView stepView;
    private ActionView actionView;
    private List<MandateFieldClassSAAdminView> classSAAdminViewList;

    public StepView getStepView() {
        return stepView;
    }

    public void setStepView(StepView stepView) {
        this.stepView = stepView;
    }

    public ActionView getActionView() {
        return actionView;
    }

    public void setActionView(ActionView actionView) {
        this.actionView = actionView;
    }

    public List<MandateFieldClassSAAdminView> getClassSAAdminViewList() {
        return classSAAdminViewList;
    }

    public void setClassSAAdminViewList(List<MandateFieldClassSAAdminView> classSAAdminViewList) {
        this.classSAAdminViewList = classSAAdminViewList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("stepView", stepView)
                .append("actionView", actionView)
                .append("classStepActionViewList", classSAAdminViewList)
                .toString();
    }
}
