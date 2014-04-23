package com.clevel.selos.model.view;


import com.clevel.selos.model.db.master.Action;
import com.clevel.selos.model.db.master.Step;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class MandateFieldView implements Serializable{
    private long id;
    private ActionView actionView;
    private StepView stepView;
    private String className;
    private String fieldName;
    private String fieldDesc;
    private String page;
    private boolean checked = false;
    private String minValue;
    private String maxValue;
    private String matchedValue;
    private String notMatchedValue;

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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getMatchedValue() {
        return matchedValue;
    }

    public void setMatchedValue(String matchedValue) {
        this.matchedValue = matchedValue;
    }

    public String getNotMatchedValue() {
        return notMatchedValue;
    }

    public void setNotMatchedValue(String notMatchedValue) {
        this.notMatchedValue = notMatchedValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("actionView", actionView)
                .append("stepView", stepView)
                .append("className", className)
                .append("fieldName", fieldName)
                .append("fieldDesc", fieldDesc)
                .append("page", page)
                .append("checked", checked)
                .append("minValue", minValue)
                .append("maxValue", maxValue)
                .append("matchedValue", matchedValue)
                .append("notMatchedValue", notMatchedValue)
                .toString();
    }
}
