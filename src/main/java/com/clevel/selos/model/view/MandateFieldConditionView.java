package com.clevel.selos.model.view;

import com.clevel.selos.model.MandateConditionType;
import com.clevel.selos.model.MandateDependType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class MandateFieldConditionView implements Serializable {

    private long id;
    private MandateConditionType mandateConditionType;
    private String conditionDesc;
    private String className;
    private MandateDependType dependType;
    private MandateFieldConditionView dependCondition;
    private List<MandateFieldConditionDetailView> conditionDetailViewList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MandateConditionType getMandateConditionType() {
        return mandateConditionType;
    }

    public void setMandateConditionType(MandateConditionType mandateConditionType) {
        this.mandateConditionType = mandateConditionType;
    }

    public String getConditionDesc() {
        return conditionDesc;
    }

    public void setConditionDesc(String conditionDesc) {
        this.conditionDesc = conditionDesc;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public MandateDependType getDependType() {
        return dependType;
    }

    public void setDependType(MandateDependType dependType) {
        this.dependType = dependType;
    }

    public MandateFieldConditionView getDependCondition() {
        return dependCondition;
    }

    public void setDependCondition(MandateFieldConditionView dependCondition) {
        this.dependCondition = dependCondition;
    }

    public List<MandateFieldConditionDetailView> getConditionDetailViewList() {
        return conditionDetailViewList;
    }

    public void setConditionDetailViewList(List<MandateFieldConditionDetailView> conditionDetailViewList) {
        this.conditionDetailViewList = conditionDetailViewList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("mandateConditionType", mandateConditionType)
                .append("conditionDesc", conditionDesc)
                .append("className", className)
                .append("dependType", dependType)
                .append("dependCondition", dependCondition)
                .append("conditionDetailViewList", conditionDetailViewList)
                .toString();
    }
}
