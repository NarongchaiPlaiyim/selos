package com.clevel.selos.model.view.master;

import com.clevel.selos.model.MandateConDetailType;
import com.clevel.selos.model.MandateDependConType;
import com.clevel.selos.model.MandateDependType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class MandateFieldConditionView implements Serializable {

    private long id;
    private MandateConDetailType mandateConDetailType;
    private String name;
    private String conditionDesc;
    private MandateFieldClassView mandateFieldClassView;
    private MandateDependType dependType;
    private MandateFieldConditionView dependCondition;
    private MandateDependConType dependConType;
    private List<MandateFieldConditionDetailView> conditionDetailViewList;
    private boolean needUpdate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MandateConDetailType getMandateConDetailType() {
        return mandateConDetailType;
    }

    public void setMandateConDetailType(MandateConDetailType mandateConDetailType) {
        this.mandateConDetailType = mandateConDetailType;
    }

    public String getConditionDesc() {
        return conditionDesc;
    }

    public void setConditionDesc(String conditionDesc) {
        this.conditionDesc = conditionDesc;
    }

    public MandateFieldClassView getMandateFieldClassView() {
        return mandateFieldClassView;
    }

    public void setMandateFieldClassView(MandateFieldClassView mandateFieldClassView) {
        this.mandateFieldClassView = mandateFieldClassView;
    }

    public MandateDependType getDependType() {
        return dependType;
    }

    public void setDependType(MandateDependType dependType) {
        this.dependType = dependType;
    }

    public MandateDependConType getDependConType() {
        return dependConType;
    }

    public void setDependConType(MandateDependConType dependConType) {
        this.dependConType = dependConType;
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

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }

    public void updateValues(MandateFieldConditionView view){
        id = view.id;
        name = view.name;
        mandateConDetailType = view.mandateConDetailType;
        conditionDesc = view.conditionDesc;
        mandateFieldClassView = view.mandateFieldClassView;
        dependType = view.dependType;
        dependCondition = view.dependCondition;
        dependConType = view.dependConType;
        conditionDetailViewList = view.conditionDetailViewList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("mandateConditionType", mandateConDetailType)
                .append("name", name)
                .append("conditionDesc", conditionDesc)
                .append("mandateFieldClassView", mandateFieldClassView)
                .append("dependType", dependType)
                .append("dependCondition", dependCondition)
                .append("dependConType", dependConType)
                .append("conditionDetailViewList", conditionDetailViewList)
                .append("needUpdate", needUpdate)
                .toString();
    }
}
