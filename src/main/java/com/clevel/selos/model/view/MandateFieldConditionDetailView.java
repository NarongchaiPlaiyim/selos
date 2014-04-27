package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class MandateFieldConditionDetailView implements Serializable{

    private long id;
    private MandateFieldView mandateFieldView;
    private String baseValue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MandateFieldView getMandateFieldView() {
        return mandateFieldView;
    }

    public void setMandateFieldView(MandateFieldView mandateFieldView) {
        this.mandateFieldView = mandateFieldView;
    }

    public String getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(String baseValue) {
        this.baseValue = baseValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("mandateFieldView", mandateFieldView)
                .append("baseValue", baseValue)
                .toString();
    }
}
